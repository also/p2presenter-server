/* $Id$ */

package org.p2presenter.remoting;

import java.lang.reflect.Proxy;

import org.p2presenter.messaging.LocalConnection;


public class RemoteInvocationConnection {
	private LocalConnection connection;
	private String uri;
	boolean bidirectional = false;
	
	public RemoteInvocationConnection(LocalConnection connection, String uri) {
		this.connection = connection;
		this.uri = uri;
	}
	
	public RemoteInvocationConnection(LocalConnection connection, String uri, boolean bidirectional) {
		this(connection, uri);
		this.bidirectional = bidirectional;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, RemoteObjectReference remoteProxyReference) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass, RemoteInvocationProxy.class}, new RemoteObjectInvocationHandler(connection, uri, bidirectional, remoteProxyReference));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, String variableName) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass, RemoteInvocationProxy.class}, new RemoteObjectInvocationHandler(connection, uri, bidirectional, variableName));
	}
	
	public LocalConnection getConnection() {
		return connection;
	}
}
