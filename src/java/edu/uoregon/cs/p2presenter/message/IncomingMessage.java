/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public interface IncomingMessage extends Message {
	public Connection getConnection();
}
