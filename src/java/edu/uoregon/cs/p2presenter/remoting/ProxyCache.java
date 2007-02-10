/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.lang.ref.WeakReference;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.concurrent.Callable;

import edu.uoregon.cs.p2presenter.LocalConnection;

public class ProxyCache {
	private static final String PROXY_CACHE_ATTRIBUTE_PREFIX = ProxyCache.class.getName() + "proxyCache.";
	private String uri;
	private String scope;
	private Integer proxyNumber = 1;
	private HashMap<Object, ObjectDescriptor> objectDescriptors = new HashMap<Object, ObjectDescriptor>();
	
	private ProxyCache(String uri, String scope) {
		this.uri = uri;
		this.scope = scope;
	}
	
	/** Maps from id to target*/
	private HashMap<Integer, Object> localObjects = new HashMap<Integer, Object>();
	
	private HashMap<RemoteObjectReference, WeakReference<RemoteInvocationProxy>> proxyReferences = new HashMap<RemoteObjectReference, WeakReference<RemoteInvocationProxy>>();
	
	
	public Object getTarget(Integer id) {
		return localObjects.get(id);
	}
	
	public ObjectDescriptor getObjectDescriptor(Object toProxy) throws Exception {
		synchronized (objectDescriptors) {
			ObjectDescriptor objectDescriptor = objectDescriptors.get(toProxy);
		
			if (objectDescriptor == null) {
				Class[] interfaces = toProxy.getClass().getInterfaces();
				objectDescriptor = new ObjectDescriptor(interfaces, proxyNumber++);
				objectDescriptors.put(toProxy, objectDescriptor);
				localObjects.put(objectDescriptor.getId(), toProxy);
			}
		
			return objectDescriptor;
		}
	}
	
	public RemoteInvocationProxy getProxy(LocalConnection connection, boolean bidirectional, ObjectDescriptor objectDescriptor) {
		synchronized (proxyReferences) {
			RemoteInvocationProxy proxy = null;
			WeakReference<RemoteInvocationProxy> remoteProxyReference = proxyReferences.get(objectDescriptor);
			if (remoteProxyReference != null) {
				proxy = remoteProxyReference.get();
			}
			if (proxy == null) {
				Class[] interfaceClasses = new Class[objectDescriptor.getProxiedClasses().length + 1];
				System.arraycopy(objectDescriptor.getProxiedClasses(), 0, interfaceClasses, 1, objectDescriptor.getProxiedClasses().length);
				interfaceClasses[0] = RemoteInvocationProxy.class;
				proxy = (RemoteInvocationProxy) Proxy.newProxyInstance(interfaceClasses[0].getClassLoader(), interfaceClasses, new RemoteObjectInvocationHandler(connection, uri, bidirectional, new RemoteObjectReference(objectDescriptor), scope));
				proxyReferences.put(objectDescriptor.getRemoteObjectReference(), new WeakReference<RemoteInvocationProxy>(proxy));
			}
			
			return proxy;
		}
	}
	
	public static ProxyCache getProxyCache(LocalConnection connection, String uri, String scope) {
		if (scope == null) {
			scope = "";
		}
		return (ProxyCache) connection.getAttribute(PROXY_CACHE_ATTRIBUTE_PREFIX  + uri + scope, new CreateProxyCacheCallable(uri, scope));
	}
	
	public static class CreateProxyCacheCallable implements Callable {
		private String uri;
		private String scope;
		
		public CreateProxyCacheCallable(String uri, String scope) {
			this.uri = uri;
			this.scope = scope;
		}

		public Object call() {
			return new ProxyCache(uri, scope);
		}
	}
}
