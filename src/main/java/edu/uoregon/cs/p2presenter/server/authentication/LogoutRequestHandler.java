/* $Id$ */

package edu.uoregon.cs.p2presenter.server.authentication;

import org.p2presenter.messaging.handler.RequestHandler;
import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;

/** Logs users out
 * .
 * @author Ryan Berdeen
 *
 */
public class LogoutRequestHandler implements RequestHandler {

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		request.setAttribute(SecurityContextIntegrationFilter.SECURITY_CONTEXT_ATTRIBUTE_NAME, null);
		return null;
	}

}
