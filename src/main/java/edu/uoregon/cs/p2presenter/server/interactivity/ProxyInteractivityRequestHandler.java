/* $Id$ */

package edu.uoregon.cs.p2presenter.server.interactivity;

import org.p2presenter.messaging.LocalConnection;
import org.p2presenter.messaging.handler.AbstractProxyRequestHandler;
import org.p2presenter.messaging.message.IncomingRequestMessage;

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
