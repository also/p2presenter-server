/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteJshInvocationHandler implements InvocationHandler {
	private String remoteVariableName;
	private JshClient client;
	
	private boolean invokeSimple;
	
	public RemoteJshInvocationHandler(JshClient client, Class interfaceClass, String remoteVariableName) {
		this.remoteVariableName = remoteVariableName;
		this.client = client;
		
		boolean canSendAllParametersAsStrings = true;
		boolean canSerializeAllParameters = true;
		
		for (Method method : interfaceClass.getMethods()) {
			for (Class<?> parameterType : method.getParameterTypes()) {
				if (!JshClient.canSendAsString(parameterType)) {
					canSendAllParametersAsStrings = false;
					if (!canSerializeAllParameters) {
						break;
					}
				}
				else if (!Serializable.class.isAssignableFrom(parameterType)) {
					canSerializeAllParameters = false;
					if (!canSendAllParametersAsStrings) {
						break;
					}
				}
			}
		}
		
		if (canSendAllParametersAsStrings) {
			invokeSimple = true;
		}
		else if (canSerializeAllParameters) {
			invokeSimple = false;
		}
		else {
			throw new IllegalArgumentException("Invalid parameter type");
		}
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (invokeSimple) {
			String[] simpleArgs = null;
			if (args != null) {
				simpleArgs = new String[args.length];
			
				for (int i = 0; i < args.length; i++) {
					simpleArgs[i] = toArgumentString(args[i]);
				}
			}
			
			return client.invokeSimple(remoteVariableName + '.' + method.getName(), simpleArgs);
		}
		
		else {
			return client.invoke(remoteVariableName + '.' + method.getName(), args);
		}
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
