/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class RemoteObjectInvocationHandler implements InvocationHandler {
	private String remoteVariableName;
	private RemoteObjectReference remoteProxyReference;
	private RemoteInvocationConnection client;
	
	public RemoteObjectInvocationHandler(RemoteInvocationConnection client, RemoteObjectReference remoteProxyReference) {
		this.client = client;
		this.remoteProxyReference = remoteProxyReference;
	}
	
	public RemoteObjectInvocationHandler(RemoteInvocationConnection client, String remoteVariableName) {
		this.client = client;
		this.remoteVariableName = remoteVariableName;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("getRemoteObjectReference") && method.getParameterTypes().length == 0) {
			return remoteProxyReference;
		}
		else if (remoteVariableName != null) {
			return client.invoke(remoteVariableName, method, args);
		}
		else {
			return client.invoke(remoteProxyReference, method, args);
		}
	}
}
