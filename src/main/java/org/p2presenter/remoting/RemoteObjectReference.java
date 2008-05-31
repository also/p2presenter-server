/* $Id$ */

package org.p2presenter.remoting;

import java.io.Serializable;

public class RemoteObjectReference implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;

	public RemoteObjectReference(int id) {
		this.id = id;
	}
	
	public RemoteObjectReference(RemoteObjectReference that) {
		this.id = that.id;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RemoteObjectReference && ((RemoteObjectReference) obj).id == id;
	}
	
	@Override
	public int hashCode() {
		// TODO not a very good hash
		return id;
	}
}
