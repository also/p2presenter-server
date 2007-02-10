/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.server;

import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.interactivity.ActiveInteractivity;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;

public class GetInteractivityRequestHandler implements RequestHandler {
	private ActiveInteractivityController activeInteractivityController;
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		Integer interactivityId = new Integer(request.getAttribute("interactivityId").toString());
		
		ActiveInteractivity<?> activeInteractivity = activeInteractivityController.getActiveInteractivity(interactivityId);
		if (activeInteractivity != null) {
			JsonObject responseObject = new JsonObject(activeInteractivity.getInteractivityDefinition(), "participantViewClassName", "participantModelInterfaceClassName");
			
			OutgoingResponseMessage response = new OutgoingResponseMessage(request);
			response.setContent(responseObject.toString());
			
			return response;
		}
		else {
			return new OutgoingResponseMessage(request, 404);
		}
	}

}
