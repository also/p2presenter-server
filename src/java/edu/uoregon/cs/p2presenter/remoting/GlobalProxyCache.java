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
		private HashMap<Object, ObjectDescriptor> objectDescriptors = new HashMap<Object, ObjectDescriptor>();
		
		/** Maps from id to target*/
		private HashMap<Integer, Object> localObjects = new HashMap<Integer, Object>();
		
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
	}
}
