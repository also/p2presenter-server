/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.host;

import org.ry1.json.JsonObject;

import com.ryanberdeen.djava.postal.PostalDJavaConnection;
import com.ryanberdeen.postal.Connection;
import com.ryanberdeen.postal.ConnectionLifecycleListener;
import com.ryanberdeen.postal.handler.RequestHandler;
import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityController;

public class JoinInteractivityRequestHandler implements RequestHandler {
	private InteractivityController interactivityController;
	private String proxyCacheUri;

	public JoinInteractivityRequestHandler(InteractivityController interactivityController, String proxyCacheUri) {
		this.interactivityController = interactivityController;
		this.proxyCacheUri = proxyCacheUri;
	}

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		Object model = interactivityController.onConnect();
		request.getConnection().addConnectionLifecycleListener(new InteractivityConnectionListener(model));
		
		PostalDJavaConnection dJavaConnection = PostalDJavaConnection.getPostalDJavaConnection(request.getConnection(), proxyCacheUri, true);
		
		OutgoingResponseMessage response = new OutgoingResponseMessage(request);
		
		JsonObject responseObject = new JsonObject();
		responseObject.set("participantModelProxyId", dJavaConnection.getObjectDescriptor(model).getId());
		
		response.setContent(responseObject.toString());
		
		return response;
	}

	/** Notifies the interactivity host when a participant disconnects.
	 * @param <T> the model type
	 */
	private class InteractivityConnectionListener implements ConnectionLifecycleListener {
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
