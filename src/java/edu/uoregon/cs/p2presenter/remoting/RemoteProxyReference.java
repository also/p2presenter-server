/* $Id$ */

package edu.uoregon.cs.p2presenter.remoting;

import java.io.Serializable;

public class RemoteProxyReference implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;

	public RemoteProxyReference(int id) {
		this.id = id;
	}
	
	public RemoteProxyReference(RemoteProxyReference that) {
		this.id = that.id;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RemoteProxyReference && ((RemoteProxyReference) obj).id == id;
	}
	
	@Override
	public int hashCode() {
		// TODO not a very good hash
		return id;
	}
}
