/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.io.Serializable;

class ProxyDescriptor extends RemoteProxyReference implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Class[] proxiedClasses;
	
	public ProxyDescriptor(Class[] proxiedClasses, int id) {
		super(id);
		this.proxiedClasses = proxiedClasses;
	}
	
	public Class<?>[] getProxiedClasses() {
		return proxiedClasses;
	}
}
