/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

public class InvocationRequestHandler implements RequestHandler {
	public static final String PARAMETER_TYPES_HEADER_SEPARATOR = ",";
	public static final String METHOD_NAME_HEADER_NAME = "Method-Name";
	public static final String TARGET_PROXY_ID_HEADER_NAME = "Target-Proxy-Id";
	public static final String TARGET_NAME_HEADER_NAME = "Target-Name";
	public static final String PARAMETER_TYPES_HEADER_NAME = "Parameter-Types";
	public static final String CONTENT_TYPE = "application/x-java-serialized-object";
	public static final String ARGUMENT_COUNT_HEADER_NAME = "Argument-Count";
	
	private InvocationListener invocationListener;
	
	public void setInvocationListener(InvocationListener invocationListener) {
		this.invocationListener = invocationListener;
	}

	// TODO ensure required headers are set
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) {
		OutgoingSerializedObjectResponseMessage response = new OutgoingSerializedObjectResponseMessage(request);
		
		Connection connection = request.getConnection();
		ProxyCache proxyCache = ProxyCache.getProxyCache(connection, request.getUri());
		
		try {
			try {
				Object result;
				String argumentCountHeader = request.getHeader(ARGUMENT_COUNT_HEADER_NAME);
				int argumentCount = Integer.parseInt(argumentCountHeader);
				
				Object[] args;
				Class<?>[] parameterTypes;
				if (argumentCount > 0) {
					args = new Object[argumentCount];
					
					ByteArrayInputStream bytes = new ByteArrayInputStream(request.getContent());
					
					try {
						ObjectInputStream in = new ObjectInputStream(bytes);
						for (int i = 0; i < args.length; i++) {
							args[i] = in.readObject();
							if (args[i] instanceof RemoteObjectReference) {
								if (args[i] instanceof ObjectDescriptor) {
									// TODO 
									args[i] = proxyCache.getProxy(connection, true, (ObjectDescriptor) args[i]);
								}
								else {
									args[i] = proxyCache.getTarget(((RemoteObjectReference) args[i]).getId());
								}
							}
						}
						in.close();
					}
					catch (IOException ex) {
						// an io exceptions should never happen while using a byte arry stream
						throw new Error(ex);
					}
					catch (ClassNotFoundException ex) {
						// FIXME
						throw new RuntimeException(ex);
					}
					
					String[] parameterTypeNames = request.getHeader(PARAMETER_TYPES_HEADER_NAME).split(PARAMETER_TYPES_HEADER_SEPARATOR);
					parameterTypes = new Class[parameterTypeNames.length];
					
					for (int i = 0; i < parameterTypes.length; i++) {
						parameterTypes[i] = Class.forName(parameterTypeNames[i]);
					}
				}
				else {
					args = new Object[0];
					parameterTypes = new Class[0];
				}
				
				Object target;
				String targetName = request.getHeader(TARGET_NAME_HEADER_NAME);
				if (targetName != null) {
					// TODO the target should come from somewhere else
					target = request.getConnection().getAttribute(targetName);
				}
				else {
					target = proxyCache.getTarget(new Integer(request.getHeader(TARGET_PROXY_ID_HEADER_NAME)));
				}
				
				Class<?> targetClass = target.getClass();
				Method method = targetClass.getMethod(request.getHeader(METHOD_NAME_HEADER_NAME), parameterTypes);
				// TODO why is this necessary
				method.setAccessible(true);
				
				// TODO unwrap invocation target exception
				
				if (invocationListener != null) {
					Object before = invocationListener.beforeMethodInvocation(target, method, args);
					result = method.invoke(target, args);
					invocationListener.afterMethodInvocation(before, result);
				}
				else {
					result = method.invoke(target, args);
				}
				
				result = method.invoke(target, args);
				
				if (result != null) {
					Class returnType = method.getReturnType();
					// TODO probably shouldn't serialize enums
					// TODO serialize wrapper types when method doesn't return primitive
					if (returnType == String.class || returnType.isPrimitive() || Enum.class.isAssignableFrom(returnType)) {
						response.setContentObject((Serializable) result);
					}
					else {
						response.setContentObject(proxyCache.getObjectDescriptor(result));	
					}
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				response.setStatus(500);
				response.setContentObject(ex);
				// TODO send the exception message if it can't be serialized
			}
		}
		catch (ObjectStreamException ex) {
			/*try {
				response.setStatus(500);
				response.setContentObject(ex);
				// TODO warn
			}
			catch (ObjectStreamException exx) {}*/
		}
		return response;
	}
}
