package edu.uoregon.cs.p2presenter.jsh;

public class ProxiedObject {
	private ProxyIdentifier proxyId;
	private Object proxy;
	
	public ProxiedObject(ProxyIdentifier proxyId, Object proxy) {
		this.proxyId = proxyId;
		this.proxy = proxy;
	}
	
	public ProxyIdentifier getProxyId() {
		return proxyId;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof ProxiedObject && ((ProxiedObject) obj).proxy == proxy;
	}
	
	@Override
	public int hashCode() {
		return System.identityHashCode(proxy);
	}
}
