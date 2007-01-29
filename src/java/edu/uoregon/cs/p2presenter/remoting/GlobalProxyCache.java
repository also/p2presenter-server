/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.util.HashMap;

class GlobalProxyCache {
	private HashMap<String, ProxyCache> caches = new HashMap<String, ProxyCache>();
	
	private Integer proxyNumber = 1;
	
	public ProxyCache getProxyCache(String name) {
		ProxyCache result = caches.get(name);
		if (result == null) {
			result = new ProxyCache();
			caches.put(name, result);
		}
		
		return result;
	}
	
	public class ProxyCache {
		private HashMap<Object, ProxyDescriptor> proxyIdentifiers = new HashMap<Object, ProxyDescriptor>();
		
		/** Maps from id to target*/
		private HashMap<Integer, Object> proxiedObjects = new HashMap<Integer, Object>();
		
		public Object getTarget(Integer id) {
			return proxiedObjects.get(id);
		}
		
		public ProxyDescriptor getProxyIdentifier(Object toProxy) throws Exception {
			synchronized (proxyIdentifiers) {
				ProxyDescriptor proxyIdentifier = proxyIdentifiers.get(toProxy);
			
				if (proxyIdentifier == null) {
					Class[] interfaces = toProxy.getClass().getInterfaces();
					proxyIdentifier = new ProxyDescriptor(interfaces, proxyNumber++);
					proxyIdentifiers.put(toProxy, proxyIdentifier);
					proxiedObjects.put(proxyIdentifier.getId(), toProxy);
				}
			
				return proxyIdentifier;
			}
		}
	}
}
