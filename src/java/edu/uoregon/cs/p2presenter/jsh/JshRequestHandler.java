/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.ParseException;
import bsh.Primitive;
import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestMessage;

public class JshRequestHandler implements RequestHandler {
	public static final String CONTENT_TYPE = "java/serialized-object";
	
	private static final String VARIABLE_NAME = "temp_args";
	
	private Integer proxyNumber = 1;
	
	private HashMap<Object, ProxyIdentifier> proxyIds = new HashMap<Object, ProxyIdentifier>();
	
	private Interpreter interpreter = new Interpreter();
	
	public JshRequestHandler(Connection connection) {
		connection.setProperty("interpreter", interpreter);
	}
	
	public static Interpreter getInterpreter(Connection connection) {
		return (Interpreter) connection.getProperty("interpreter");
	}
	
	public OutgoingResponseMessage handleRequest(RequestMessage request) {
		OutgoingSerializedObjectResponseMessage response = new OutgoingSerializedObjectResponseMessage(request);
		try {
			try {
				Object result;
				String argumentCountHeader = request.getHeader("Argument-Count");
				if (argumentCountHeader != null) {
					String argumentString;
					
					int argumentCount = Integer.parseInt(argumentCountHeader);
					
					if (argumentCount > 0) {
						Object[] args = new Object[argumentCount];
						
						ByteArrayInputStream bytes = new ByteArrayInputStream(request.getContent());
						
						try {
							ObjectInputStream in = new ObjectInputStream(bytes);
							for (int i = 0; i < args.length; i++) {
								args[i] = in.readObject();
							}
							in.close();
						}
						catch (IOException ex) {
							// FIXME
							throw new Error(ex);
						}
						catch (ClassNotFoundException ex) {
							// FIXME
							throw new Error(ex);
						}
						
						interpreter.set(VARIABLE_NAME, args);
						
						StringBuilder argumentStringBuilder = new StringBuilder();
						appendArgumentString(argumentStringBuilder, args, 0);
						
						for (int i = 1; i < args.length; i++) {
							argumentStringBuilder.append(',');
							appendArgumentString(argumentStringBuilder, args, i);
						}
						
						argumentString = argumentStringBuilder.toString();
					}
					else {
						argumentString = "";
					}
					
					result = interpreter.eval(request.getHeader("Method-Name") + '(' + argumentString + ')');
					interpreter.unset(VARIABLE_NAME);
				}
				else {
					result = interpreter.eval(request.getContentAsString());
				}
				if (result != null) {
					Class resultType = result.getClass();
					// TODO probably shouldn't serialize enums
					// TODO shouldn't be ignoring required return type when the result is a wrapper
					if (resultType == CharSequence.class || resultType == String.class|| Primitive.isWrapperType(resultType) || Enum.class.isAssignableFrom(resultType)) {
						response.setContentObject((Serializable) result);
					}
					else {
						Class<?> requiredReturnType = null;
						String requiredReturnTypeName = request.getHeader("Return-Class");
						if (requiredReturnTypeName != null) {
							try {
								requiredReturnType = Class.forName(requiredReturnTypeName, false, getClass().getClassLoader());
								
								if (requiredReturnType.isAssignableFrom(resultType)) {
									response.setContentObject(getOrGenerateProxyId(result, requiredReturnType));
								}
								else {
									response.setStatus(500);
									response.setContentObject(new RemoteException("Can't proxy " + resultType.getName() + " as " + requiredReturnType.getName()));
								}
							}
							catch (ClassNotFoundException ex) {
								ex.printStackTrace();
								response.setStatus(500);
								response.setContentObject(new RemoteException("Unknown remote type", ex));
							}
						}
					}
				}
					
			}
			catch (ParseException ex) {
				response.setStatus(400);
				response.setContentObject(ex);
			}
			catch (EvalError ex) {
				ex.printStackTrace();
				response.setStatus(500);
				response.setContentObject(ex);
			}
		}
		catch (ObjectStreamException ex) {
			try {
				response.setStatus(500);
				response.setContentObject(ex);
			}
			catch (ObjectStreamException exx) {}
		}
		return response;
	}
	
	private ProxyIdentifier getOrGenerateProxyId(Object toProxy, Class proxyClass) throws EvalError {
		ProxyIdentifier proxyId = proxyIds.get(toProxy);
		
		if (proxyId == null) {
			synchronized (proxyIds) {
				interpreter.set("proxy" + proxyNumber, toProxy);
				proxyId = new ProxyIdentifier(proxyClass, proxyNumber++);
				proxyIds.put(toProxy, proxyId);
			}
		}
		
		return proxyId;
	}
	
	private void appendArgumentString(StringBuilder builder, Object[] args, int index) {
		Object arg = args[index];
		if (arg instanceof ProxyIdentifier) {
			ProxyIdentifier proxyId = (ProxyIdentifier) arg;
			builder.append("proxy" + proxyId.getId());
		}
		else {
			builder.append(VARIABLE_NAME);
			builder.append('[').append(index).append(']');
		}
	}
}
