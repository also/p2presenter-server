/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.io.Serializable;

public class RemoteProxyReference implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int proxyId;

	public RemoteProxyReference(int proxyId) {
		this.proxyId = proxyId;
	}

	public int getProxyId() {
		return proxyId;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RemoteProxyReference && ((RemoteProxyReference) obj).proxyId == proxyId;
	}
	
	@Override
	public int hashCode() {
		// TODO not a very good hash
		return proxyId;
	}
}
