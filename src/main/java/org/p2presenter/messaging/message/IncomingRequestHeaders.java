/*$Id$ */

package org.p2presenter.messaging.message;

import org.p2presenter.messaging.Connection;

public interface IncomingRequestHeaders extends RequestHeaders, IncomingHeaders {
	public Connection getConnection();
	
	public void setAttribute(String name, Object value);
	
	public Object getAttribute(String name);
}
