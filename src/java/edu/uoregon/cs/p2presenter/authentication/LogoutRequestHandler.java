/* $Id$ */

package edu.uoregon.cs.p2presenter.authentication;

import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

public class LogoutRequestHandler implements RequestHandler {

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		request.setAttribute(SecurityContextIntegrationFilter.SECURITY_CONTEXT_ATTRIBUTE_NAME, null);
		return null;
	}

}
