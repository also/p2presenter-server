/* $Id$ */

package org.p2presenter.remoting;

import java.io.Serializable;

public class ObjectDescriptor extends RemoteObjectReference implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Class<?>[] proxiedClasses;
	
	public ObjectDescriptor(Class<?>[] proxiedClasses, int id) {
		super(id);
		this.proxiedClasses = proxiedClasses;
	}
	
	public Class<?>[] getProxiedClasses() {
		return proxiedClasses;
	}
	
	public RemoteObjectReference getRemoteObjectReference() {
		return new RemoteObjectReference(getId());
	}
}
