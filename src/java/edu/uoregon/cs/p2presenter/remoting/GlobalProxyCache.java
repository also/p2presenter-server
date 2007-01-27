/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.util.HashMap;

public class GlobalProxyCache {
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
		private HashMap<ProxiedObject, ProxyIdentifier> proxyIds = new HashMap<ProxiedObject, ProxyIdentifier>();
		
		/** Maps from id to target*/
		private HashMap<Integer, Object> proxiedObjects = new HashMap<Integer, Object>();
		
		public Object getTarget(Integer id) {
			return proxiedObjects.get(id);
		}
		
		public Object getTarget(ProxyIdentifier proxyIdentifier) {
			return getTarget(proxyIdentifier.getId());
		}
		
		public ProxyIdentifier getOrGenerateProxyId(Object toProxy, Class[] proxyClasses) throws Exception {
			ProxiedObject proxiedObject = new ProxiedObject(toProxy);
			ProxyIdentifier proxyId = proxyIds.get(proxiedObject);
			
			if (proxyId == null) {
				proxiedObject.setProxyId(proxyId);
				synchronized (proxyIds) {
					proxyId = new ProxyIdentifier(proxyClasses, proxyNumber++);
					proxyIds.put(proxiedObject, proxyId);
					proxiedObjects.put(proxyId.getId(), toProxy);
				}
			}
			
			return proxyId;
		}
	}
}
