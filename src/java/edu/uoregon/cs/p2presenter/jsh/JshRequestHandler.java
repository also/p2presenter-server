/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.ParseException;
import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestMessage;

public class JshRequestHandler implements RequestHandler {
	public static final String CONTENT_TYPE = "java/serialized-object";
	
	private static final String VARIABLE_NAME = JshRequestHandler.class.getName();
	
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
			if (request.hasContent()) {
				Object result;
				if (CONTENT_TYPE.equals(request.getContentType())) {
					Object[] args = new Object[Integer.parseInt(request.getHeader("Argument-Count"))];
					
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
					String argumentString;
					
					if (args.length > 0) {
						StringBuilder argumentStringBuilder = new StringBuilder(VARIABLE_NAME);
						argumentStringBuilder.append("[0]");
						
						for (int i = 1; i < args.length; i++) {
							argumentStringBuilder.append(',');
							argumentStringBuilder.append(VARIABLE_NAME);
							argumentStringBuilder.append('[').append(i).append(']');
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
				
				if (result instanceof Serializable) {
					response.setContentObject((Serializable) result);
				}
				else if (result != null){
					response.setContent(result.toString());
				}
			}
		}
		catch (ParseException ex) {
			response.setStatus(400);
			response.setContent(ex.getMessage());
		}
		catch (EvalError ex) {
			response.setStatus(500);
			response.setContent(ex.getMessage());
		} catch (ObjectStreamException e) {
			e.printStackTrace();
		}
		return response;
	}
}
