/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.host;

import org.p2presenter.messaging.LocalConnection;
import org.p2presenter.messaging.handler.UriPatternRequestMatcher;
import org.p2presenter.messaging.message.IncomingResponseMessage;
import org.p2presenter.messaging.message.OutgoingRequestMessage;
import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityController;
import edu.uoregon.cs.p2presenter.remoting.InvocationRequestHandler;

public class InteractivityHostClient {
	private InteractivityController<?> controller;
	private LocalConnection connection;
	private int interactivityId;
	
	@SuppressWarnings("unchecked")
	public InteractivityHostClient(LocalConnection connection, int interactivityId) throws Exception {
		this.connection = connection;
		this.interactivityId = interactivityId;
		
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, "/interactivity/" + interactivityId + "/admin/get");
		IncomingResponseMessage response = connection.sendRequestAndAwaitResponse(request);
		if (response.getStatus() == 200) {
			JsonObject responseObject = JsonObject.valueOf(response.getContentAsString());
	
			Class<InteractivityController> controllerClass = (Class<InteractivityController>) Class.forName(responseObject.get("hostControllerClassName").toString());
			controller = controllerClass.newInstance();
			connection.setAttribute("interactivity", controller);
			
			JoinInteractivityRequestHandler joinHandler = new JoinInteractivityRequestHandler(controller, "/interactivity/" + interactivityId + "/controller");
			connection.getRequestHandlerMapping().mapHandler(new UriPatternRequestMatcher("/interactivity/(\\d+)/join", "interactivityId"), joinHandler);
			
			InvocationRequestHandler invoker = new InvocationRequestHandler();
			connection.getRequestHandlerMapping().mapHandler(new UriPatternRequestMatcher("/interactivity/(\\d+)/controller", "interactivityId"), invoker);
		}
		else {
			// TODO exception type
			throw new Exception();
		}
	}
	
	public InteractivityController<?> getController() {
		return controller;
	}
	
	public void begin() throws Exception {
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, "/interactivity/" + interactivityId + "/admin/begin");
		connection.sendRequestAndAwaitResponse(request);
		// TODO make sure request was successful
	}
}
