/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import java.io.IOException;

import edu.uoregon.cs.p2presenter.AbstractProxyRequestHandler;
import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionListener;
import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;

public class JoinInteractivityRequestHandler implements RequestHandler {
	private ActiveInteractivityController activeInteractivityController;
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws IOException {
		Connection connection = request.getConnection();
		Integer interactivityId = new Integer(request.getAttribute("interactivityId").toString());
		
		ActiveInteractivity<?> activeInteractivity = activeInteractivityController.getActiveInteractivity(interactivityId);
		if (activeInteractivity != null) {
			connection.addConnectionListener(new InteractivityConnectionListener(activeInteractivity));
			
			LocalConnection target = activeInteractivity.getHostConnection();
			
			AbstractProxyRequestHandler.sendProxiedRequest(target, request, request.getLocalConnection().getConnectionId());
			
			return null;
		}
		else {
			return new OutgoingResponseMessage(request, 404);
		}
	}

	/** Notifies the interactivity host when a participant disconnects.
	 * @param <T> the model type
	 */
	private static class InteractivityConnectionListener implements ConnectionListener {
		private ActiveInteractivity activeInteractivity;
		
		private InteractivityConnectionListener(ActiveInteractivity activeInteractivity) {
			this.activeInteractivity = activeInteractivity;
		}

		@SuppressWarnings("unchecked")
		public void connectionClosed(Connection connection) {
			// FIXME notify the host
		}
	}
}
