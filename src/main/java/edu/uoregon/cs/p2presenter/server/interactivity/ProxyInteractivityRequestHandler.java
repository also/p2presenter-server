package edu.uoregon.cs.p2presenter.server.interactivity;

import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.handler.AbstractProxyRequestHandler;
import com.ryanberdeen.postal.message.IncomingRequestMessage;

import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;

/** Proxies interactivity messages from participant to host.
 * @author Ryan Berdeen
 *
 */
public class ProxyInteractivityRequestHandler extends AbstractProxyRequestHandler {
	private ActiveInteractivityController activeInteractivityController;

	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}

	@Override
	protected LocalConnection getTargetConnection(IncomingRequestMessage request) {
		Integer interactivityId = new Integer(request.getAttribute("interactivityId").toString());
		LocalConnection hostConnection = activeInteractivityController.getActiveInteractivity(interactivityId).getHostConnection();
		return hostConnection;
	}
}
