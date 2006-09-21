/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteJshInvocationHandler implements InvocationHandler {
	private String remoteVariableName;
	private JshClient client;
	
	public RemoteJshInvocationHandler(JshClient client, Class interfaceClass, String remoteVariableName, boolean validateTypes) {
		this.remoteVariableName = remoteVariableName;
		this.client = client;
		
		if (validateTypes) {
			for (Method method : interfaceClass.getMethods()) {
				for (Class<?> parameterType : method.getParameterTypes()) {
					if (!canTransportType(parameterType)) {
						throw new IllegalArgumentException("Cannot transport parameter of type " + parameterType.getName());
					}
				}
				Class returnType = method.getReturnType();
				if (!canTransportType(returnType)) {
					throw new IllegalArgumentException("Cannot transport type " + returnType.getName());
				}
			}
		}
	}
	
	private boolean canTransportType(Class type) {
		// TODO enums probably shouldn't be allowed.
		return type.isPrimitive() || type.isInterface() || Enum.class.isAssignableFrom(type);
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return client.invoke(remoteVariableName, method, args);
	}
}
