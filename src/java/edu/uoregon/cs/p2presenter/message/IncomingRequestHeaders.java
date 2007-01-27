/*$Id$ */

package edu.uoregon.cs.p2presenter.message;

public interface IncomingRequestHeaders extends RequestHeaders, IncomingHeaders {
	public void setAttribute(String name, Object value);
	
	public Object getAttribute(String name);
}
