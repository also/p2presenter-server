/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.host;

import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionListener;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityController;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.remoting.ProxyCache;

public class JoinInteractivityRequestHandler implements RequestHandler {
	private InteractivityController interactivityController;
	private String proxyCacheUri;

	public JoinInteractivityRequestHandler(InteractivityController interactivityController, String proxyCacheUri) {
		this.interactivityController = interactivityController;
		this.proxyCacheUri = proxyCacheUri;
	}

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		Object model = interactivityController.onConnect();
		request.getConnection().addConnectionListener(new InteractivityConnectionListener(model));
		
		ProxyCache proxyCache = ProxyCache.getProxyCache(request.getConnection(), proxyCacheUri);
		
		OutgoingResponseMessage response = new OutgoingResponseMessage(request);
		
		JsonObject responseObject = new JsonObject();
		responseObject.set("participantModelProxyId", proxyCache.getObjectDescriptor(model).getId());
		
		response.setContent(responseObject.toString());
		
		return response;
	}

	/** Notifies the interactivity host when a participant disconnects.
	 * @param <T> the model type
	 */
	private class InteractivityConnectionListener implements ConnectionListener {
		private Object model;
		
		private InteractivityConnectionListener(Object model) {
			this.model = model;
		}

		@SuppressWarnings("unchecked")
		public void connectionClosed(Connection connection) {
			interactivityController.onDisconnect(model);
		}
	}
}
