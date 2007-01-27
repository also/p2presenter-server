/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

public class ProxiedObject {
	private ProxyIdentifier proxyId;
	private Object proxy;
	
	ProxiedObject(Object proxy) {
		this.proxy = proxy;
	}
	
	public ProxiedObject(ProxyIdentifier proxyId, Object proxy) {
		this.proxyId = proxyId;
		this.proxy = proxy;
	}
	
	public ProxyIdentifier getProxyId() {
		return proxyId;
	}
	
	void setProxyId(ProxyIdentifier proxyId) {
		this.proxyId = proxyId;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof ProxiedObject && ((ProxiedObject) obj).proxy == proxy;
	}
	
	@Override
	public int hashCode() {
		// Allows proxied objects to be added to hashes without remote invocation of hashCode
		return System.identityHashCode(proxy);
	}
}
