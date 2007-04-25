/* $Id$ */

package org.p2presenter.messaging.message;

import org.p2presenter.messaging.LocalConnection;

/** Headers that have been recieved by a connection.
 * @author rberdeen
 *
 */
public interface IncomingHeaders extends Headers {
	/** Return the connection that received the headers.
	 */
	public LocalConnection getLocalConnection();
}
