/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

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
