/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionListener;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.remoting.RemoteInvocationProxy;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;
import edu.uoregon.cs.presenter.entity.InteractivityDefinition;

public class JoinInteractivityRequestHandler implements RequestHandler {
	private ActiveInteractivityController activeInteractivityController;
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) {
		Connection connection = request.getConnection();
		Integer interactivityId = new Integer(request.getAttribute("interactivityId").toString());
		
		ActiveInteractivity<?> activeInteractivity = activeInteractivityController.getActiveInteractivity(interactivityId);
		if (activeInteractivity != null) {
			InteractivityDefinition interactivityDefinition =  activeInteractivity.getInteractivityDefinition();
			
			InteractivityController interactivityController = activeInteractivity.getInteractivityController();
			Object model = interactivityController.onConnect();
			connection.addConnectionListener(new InteractivityConnectionListener(interactivityController, model));
			
			connection.getRequestHandlerMapping().mapHandler("/interactivity/" + interactivityId + "/controller", new ProxyInteractivityRequestHandler(activeInteractivityController));
			
			OutgoingResponseMessage response = new OutgoingResponseMessage(request);
			JsonObject responseObject = new JsonObject(interactivityDefinition, "participantViewClassName", "participantModelInterfaceClassName");
			responseObject.set("participantModelProxyId", ((RemoteInvocationProxy) model).getRemoteObjectReference().getId());
			response.setContent(responseObject.toString());
			
			return response;
		}
		else {
			return new OutgoingResponseMessage(request, 404);
		}
	}

	/** Calls the interactivity controller's onDisconnect method.
	 * @param <T> the model type
	 */
	private static class InteractivityConnectionListener implements ConnectionListener {
		private InteractivityController interactivityRunner;
		private Object model;
		
		public InteractivityConnectionListener(InteractivityController interactivityRunner, Object model) {
			this.interactivityRunner = interactivityRunner;
			this.model = model;
		}

		@SuppressWarnings("unchecked")
		public void connectionClosed(Connection connection) {
			interactivityRunner.onDisconnect(model);
		}
	}
}
