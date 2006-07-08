/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

/** Headers that have been recieved by a connection.
 * @author rberdeen
 *
 */
public interface IncomingHeaders extends Headers {
	/** Return the connection that received the headers.
	 */
	public Connection getConnection();
}
