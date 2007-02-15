/*$Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public interface IncomingRequestHeaders extends RequestHeaders, IncomingHeaders {
	public Connection getConnection();
	
	public void setAttribute(String name, Object value);
	
	public Object getAttribute(String name);
}
