package edu.uoregon.cs.p2presenter.jsh;

import java.io.Serializable;

public class ProxyIdentifier implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Class proxiedClass;
	
	private int id;
	
	public ProxyIdentifier(Class proxiedClass, int id) {
		this.proxiedClass = proxiedClass;
		this.id = id;
	}
	
	public Class<?> getProxiedClass() {
		return proxiedClass;
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
