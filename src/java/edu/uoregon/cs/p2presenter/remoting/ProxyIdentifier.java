/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.io.Serializable;

// TODO rename (include "descriptor" in name)
public class ProxyIdentifier implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Class[] proxiedClasses;
	
	private int id;
	
	public ProxyIdentifier(Class[] proxiedClasses, int id) {
		this.proxiedClasses = proxiedClasses;
		this.id = id;
	}
	
	public Class<?>[] getProxiedClasses() {
		return proxiedClasses;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ProxyIdentifier && ((ProxyIdentifier) obj).getId() == id);
	}
}
