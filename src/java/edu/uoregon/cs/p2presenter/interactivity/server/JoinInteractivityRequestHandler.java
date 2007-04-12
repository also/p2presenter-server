/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.server;

import java.io.IOException;

import edu.uoregon.cs.p2presenter.AbstractProxyRequestHandler;
import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.interactivity.ActiveInteractivity;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;

public class JoinInteractivityRequestHandler implements RequestHandler {
	private ActiveInteractivityController activeInteractivityController;
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws IOException {
		Integer interactivityId = new Integer(request.getAttribute("interactivityId").toString());
		
		ActiveInteractivity activeInteractivity = activeInteractivityController.getActiveInteractivity(interactivityId);
		if (activeInteractivity != null) {
			
			LocalConnection target = activeInteractivity.getHostConnection();
			
			AbstractProxyRequestHandler.sendProxiedRequest(target, request, request.getLocalConnection().getConnectionId());
			
			return null;
		}
		else {
			return new OutgoingResponseMessage(request, 404);
		}
	}
}
