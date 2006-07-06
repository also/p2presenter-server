/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

/** A message that has been recieved by a connection.
 * @author rberdeen
 *
 */
public interface IncomingMessage extends Message {
	/** Return the connection that received the message.
	 */
	public Connection getConnection();
}
