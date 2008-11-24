package edu.uoregon.cs.p2presenter.server.authentication;

import com.ryanberdeen.postal.handler.RequestHandler;
import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;

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
