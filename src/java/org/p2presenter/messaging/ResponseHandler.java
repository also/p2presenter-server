/* $Id$ */

package org.p2presenter.messaging;

import org.p2presenter.messaging.message.IncomingResponseMessage;

/** Handles a response.
 * @author rberdeen
 *
 * @param <V> the result type of the <code>handleResponse</code> method
 */
public interface ResponseHandler<V> {
	public V handleResponse(IncomingResponseMessage response) throws Exception;
}
