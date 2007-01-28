/* $Id:RemoteJshInvocationHandler.java 84 2007-01-21 01:09:29Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.p2presenter.remoting;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteInvocationHandler implements InvocationHandler {
	private String remoteVariableName;
	private RemoteProxyReference remoteProxyReference;
	private RemoteInvocationConnection client;
	
	public RemoteInvocationHandler(RemoteInvocationConnection client, RemoteProxyReference remoteProxyReference) {
		this.client = client;
		this.remoteProxyReference = remoteProxyReference;
	}
	
	public RemoteInvocationHandler(RemoteInvocationConnection client, String remoteVariableName) {
		this.client = client;
		this.remoteVariableName = remoteVariableName;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("getRemoteProxyReference") && method.getParameterTypes().length == 0) {
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
