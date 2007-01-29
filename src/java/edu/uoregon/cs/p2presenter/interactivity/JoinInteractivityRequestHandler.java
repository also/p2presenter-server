/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

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
	
	public JoinInteractivityRequestHandler() {}
	
	public JoinInteractivityRequestHandler(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) {
		Connection connection = request.getConnection();
		Integer interactivityId = (Integer) request.getAttribute(InteractivityRequestMatcher.INTERACTIVITY_ID_ATTRIBUTE_NAME);
		
		ActiveInteractivity<?> activeInteractivity = activeInteractivityController.getActiveInteractivity(interactivityId);
		InteractivityDefinition interactivityDefinition =  activeInteractivity.getInteractivityDefinition();
		InteractivityRunner<?> interactivityRunner = activeInteractivity.getInteractivityRunner();
		
		Object model = interactivityRunner.onConnect();
		connection.addConnectionListener(new InteractivityConnectionListener(interactivityRunner, model));
		
		connection.getRequestHandlerMapping().mapHandler(InteractivityRequestMatcher.URI_PREFIX + interactivityId + "/controller", new ProxyInteractivityRequestHandler(activeInteractivityController));
		
		OutgoingResponseMessage response = new OutgoingResponseMessage(request);
		response.setHeader(InteractivityClient.CLIENT_VIEW_CLASS_NAME_HEADER_NAME, interactivityDefinition.getClientViewClassName());
		response.setHeader(InteractivityClient.MODEL_CLASS_NAME_HEADER_NAME, interactivityDefinition.getClientModelClassName());
		response.setHeader(InteractivityClient.MODEL_PROXY_ID_HEADER_NAME, String.valueOf(((RemoteInvocationProxy) model).getRemoteProxyReference().getId()));
		return response;
	}

	/** Calls the interactivity controller's onDisconnect method.
	 * @param <T> the model type
	 */
	private static class InteractivityConnectionListener implements ConnectionListener {
		private InteractivityRunner interactivityRunner;
		private Object model;
		
		public InteractivityConnectionListener(InteractivityRunner<?> interactivityRunner, Object model) {
			this.interactivityRunner = interactivityRunner;
			this.model = model;
		}

		@SuppressWarnings("unchecked")
		public void connectionClosed(Connection connection) {
			interactivityRunner.onDisconnect(model);
		}
	}
}
