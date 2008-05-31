/* $Id$ */

package org.p2presenter.messaging.handler;

import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;

/** Handles a request.
 * @author rberdeen
 *
 */
public interface RequestHandler {
	/**
	 * Handle the request. Returns the response, with null indicating no
	 * response is necessary or that a response has already been sent.
	 */
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception;
}
