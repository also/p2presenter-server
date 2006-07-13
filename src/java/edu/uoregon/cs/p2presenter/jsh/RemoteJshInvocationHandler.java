/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;

import bsh.Primitive;

public class RemoteJshInvocationHandler implements InvocationHandler {
	private String remoteVariableName;
	private JshClient client;
	
	public RemoteJshInvocationHandler(JshClient client, Class interfaceClass, String remoteVariableName) {
		this.remoteVariableName = remoteVariableName;
		this.client = client;
		
		for (Method method : interfaceClass.getMethods()) {
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
		
		return client.eval(methodCall.toString());
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
