/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;

/** Handles a response.
 * @author rberdeen
 *
 * @param <V> the result type of the <code>handleResponse</code> method
 */
public interface ResponseHandler<V> {
	public V handleResponse(IncomingResponseMessage response) throws Exception;
}
