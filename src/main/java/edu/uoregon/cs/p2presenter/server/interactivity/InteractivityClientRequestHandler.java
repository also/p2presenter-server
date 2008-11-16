/* $Id$ */

package edu.uoregon.cs.p2presenter.server.interactivity;

import org.ry1.json.JsonObject;

import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.handler.AbstractProxyRequestHandler;
import com.ryanberdeen.postal.handler.RequestHandler;
import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;

import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;

/** Handles requests from interactivity clients.
 * @author Ryan Berdeen
 *
 */
public class InteractivityClientRequestHandler implements RequestHandler {
	private ActiveInteractivityController activeInteractivityController;
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		Integer interactivityId = new Integer(request.getAttribute("interactivityId").toString());
		String action = (String) request.getAttribute("action");
		
		ActiveInteractivity activeInteractivity = activeInteractivityController.getActiveInteractivity(interactivityId);
		if (activeInteractivity != null) {
			if (action.equals("get")) {
				JsonObject responseObject = new JsonObject(activeInteractivity.getInteractivityDefinition(), "participantViewClassName", "participantModelInterfaceClassName");
			
				OutgoingResponseMessage response = new OutgoingResponseMessage(request);
				response.setContent(responseObject.toString());
				
				return response;
			}
			else if (action.equals("join")) {
				LocalConnection target = activeInteractivity.getHostConnection();
				
				// join request are proxied directly to the host
				AbstractProxyRequestHandler.sendProxiedRequest(target, request, request.getLocalConnection().getConnectionId());
				
				// response will handled by sendProxiedRequest
				return null;
			}
		}
		
		// either the interactivity id or the action were invalid
		OutgoingResponseMessage response = new OutgoingResponseMessage(request, 404);
		response.setContent("The interactivity does not exist.");
		return response;
	}

}
