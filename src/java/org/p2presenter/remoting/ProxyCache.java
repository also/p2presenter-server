/* $Id$ */

package org.p2presenter.remoting;

import java.lang.ref.WeakReference;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.p2presenter.messaging.Connection;


public class ProxyCache {
	private static final String PROXY_CACHE_ATTRIBUTE_PREFIX = ProxyCache.class.getName() + "proxyCache.";
	private String uri;
	private Integer proxyNumber = 1;
	private HashMap<Object, ObjectDescriptor> objectDescriptors = new HashMap<Object, ObjectDescriptor>();
	
	private ProxyCache(String uri) {
		this.uri = uri;
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
				Class<?>[] interfaces = toProxy.getClass().getInterfaces();
				objectDescriptor = new ObjectDescriptor(interfaces, proxyNumber++);
				objectDescriptors.put(toProxy, objectDescriptor);
				localObjects.put(objectDescriptor.getId(), toProxy);
			}
		
			return objectDescriptor;
		}
	}
	
	public RemoteInvocationProxy getProxy(Connection connection, boolean bidirectional, ObjectDescriptor objectDescriptor) {
		synchronized (proxyReferences) {
			RemoteInvocationProxy proxy = null;
			WeakReference<RemoteInvocationProxy> remoteProxyReference = proxyReferences.get(objectDescriptor);
			if (remoteProxyReference != null) {
				proxy = remoteProxyReference.get();
			}
			if (proxy == null) {
				Class<?>[] interfaceClasses = new Class[objectDescriptor.getProxiedClasses().length + 1];
				System.arraycopy(objectDescriptor.getProxiedClasses(), 0, interfaceClasses, 1, objectDescriptor.getProxiedClasses().length);
				interfaceClasses[0] = RemoteInvocationProxy.class;
				proxy = (RemoteInvocationProxy) Proxy.newProxyInstance(interfaceClasses[0].getClassLoader(), interfaceClasses, new RemoteObjectInvocationHandler(connection, uri, bidirectional, new RemoteObjectReference(objectDescriptor)));
				proxyReferences.put(objectDescriptor.getRemoteObjectReference(), new WeakReference<RemoteInvocationProxy>(proxy));
			}
			
			return proxy;
		}
	}
	
	public static ProxyCache getProxyCache(Connection connection, String uri) {
		return (ProxyCache) connection.getAttribute(PROXY_CACHE_ATTRIBUTE_PREFIX  + uri, new CreateProxyCacheCallable(uri));
	}
	
	public static class CreateProxyCacheCallable implements Callable<Object> {
		private String uri;
		
		public CreateProxyCacheCallable(String uri) {
			this.uri = uri;
		}

		public Object call() {
			return new ProxyCache(uri);
		}
	}
}
