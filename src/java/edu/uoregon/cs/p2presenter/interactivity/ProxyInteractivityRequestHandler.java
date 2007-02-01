/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import edu.uoregon.cs.p2presenter.AbstractProxyRequestHandler;
import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;

public class ProxyInteractivityRequestHandler extends AbstractProxyRequestHandler {
	private ActiveInteractivityController activeInteractivityController;
	
	public ProxyInteractivityRequestHandler() {}
	
	public ProxyInteractivityRequestHandler(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}

	@Override
	protected Connection getTargetConnection(IncomingRequestMessage request) {
		Integer interactivityId = new Integer(request.getAttribute("interactivityId").toString());
		return activeInteractivityController.getActiveInteractivity(interactivityId).getHostConnection();
	}
}
