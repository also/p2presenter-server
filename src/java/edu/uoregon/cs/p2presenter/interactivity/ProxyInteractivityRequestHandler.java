/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import edu.uoregon.cs.p2presenter.AbstractProxyRequestHandler;
import edu.uoregon.cs.p2presenter.ConnectionManager;
import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;

public class ProxyInteractivityRequestHandler extends AbstractProxyRequestHandler {
	private ActiveInteractivityController activeInteractivityController;
	private ConnectionManager connectionManager;
	
	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}

	@Override
	protected LocalConnection getTargetConnection(IncomingRequestMessage request) {
		Integer interactivityId = new Integer(request.getAttribute("interactivityId").toString());
		LocalConnection hostConnection = activeInteractivityController.getActiveInteractivity(interactivityId).getHostConnection();
		
		if (request.getConnection() == hostConnection) {
			return connectionManager.getConnection(new Integer(request.getHeader("Target-Connection-Id")));
		}
		return hostConnection;
	}
}
