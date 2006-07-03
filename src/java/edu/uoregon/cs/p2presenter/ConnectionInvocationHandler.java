/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;

import bsh.Primitive;

public class ConnectionInvocationHandler implements InvocationHandler {
	private String remoteVariableName;
	private Connection connection;
	
	public ConnectionInvocationHandler(Connection connection, Class interfaceClass, String remoteVariableName) {
		this.remoteVariableName = remoteVariableName;
		this.connection = connection;
		
		for (Method method : interfaceClass.getMethods()) {
			if (!canTransferType(method.getReturnType())) {
				throw new IllegalArgumentException("Unsupported return type in " + method.getName());
			}
			
			for (Class<?> parameterType : method.getParameterTypes()) {
				if (!canTransferType(parameterType)) {
					throw new IllegalArgumentException("Unsupported parameter type in " + method.getName());
				}
			}
		}
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		StringBuilder methodCall = new StringBuilder(remoteVariableName);
		methodCall.append('.');
		methodCall.append(method.getName());
		methodCall.append('(');
		
		if (args != null) {
			methodCall.append(toArgumentString(args[0]));
			
			for (int i = 1; i < args.length; i++) {
				methodCall.append(',').append(toArgumentString(args[1]));
			}
		}
		
		methodCall.append(");");
		
		OutgoingRequestMessage message = new OutgoingRequestMessage(connection.generateMessageId());
		message.setContent(methodCall.toString());
		
		connection.write(message);
		ResponseMessage response = null;
		
		while (response == null) {
			try {
				response = connection.awaitResponse(message);
			}
			catch (InterruptedException ex) { 
				// FIXME
			}
		}
		
		String content = response.getContentAsString();
		
		if (content != null) {
			Class<?> returnType = method.getReturnType();
			if (returnType == Short.TYPE) {
				return new Short(content);
			}
			else if (returnType == Integer.TYPE) {
				return new Integer(content);
			}
			else if (returnType == Long.TYPE) {
				return new Long(content);
			}
			else if (returnType == Float.TYPE) {
				return new Float(content);
			}
			else if (returnType == Double.TYPE) {
				return new Double(content);
			}
			else if (returnType == Boolean.TYPE) {
				return new Boolean(content);
			}
			else if (returnType == Character.TYPE) {
				return new Character(content.charAt(0));
			}
			else if (returnType == Byte.TYPE) {
				return new Byte(content);
			}
 		}
		
		return null;
	}
	
	private static boolean canTransferType(Class<?> type) {
		return type == Void.TYPE || type == CharSequence.class || type == String.class || Primitive.isWrapperType(type);
	}
	
	private static String toArgumentString(Object argument) {
		if(argument instanceof CharSequence) {
			return '"' + argument.toString() + '"';
		}
		else {
			return String.valueOf(argument);
		}
	}

}
