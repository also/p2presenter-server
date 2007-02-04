/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.UriPatternRequestMatcher;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;
import edu.uoregon.cs.p2presenter.remoting.InvocationRequestHandler;

public class InteractivityHostClient {
	private InteractivityController<?> controller;
	private LocalConnection connection;
	private int interactivityId;
	
	@SuppressWarnings("unchecked")
	public InteractivityHostClient(LocalConnection connection, int interactivityId) throws Exception {
		this.connection = connection;
		this.interactivityId = interactivityId;
		
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.GET, "/interactivity/" + interactivityId + "/admin");
		request.setHeader("Action", "get");
		IncomingResponseMessage response = connection.sendRequestAndAwaitResponse(request);
		if (response.getStatus() == 200) {
			JsonObject responseObject = JsonObject.valueOf(response.getContentAsString());
	
			Class<InteractivityController> controllerClass = (Class<InteractivityController>) Class.forName(responseObject.get("hostControllerClassName").toString());
			controller = controllerClass.newInstance();
			
			InvocationRequestHandler invoker = new InvocationRequestHandler();
			connection.getRequestHandlerMapping().mapHandler(new UriPatternRequestMatcher("/interactivity/(\\d+)/controller", "interactivityId"), invoker);
			connection.setAttribute("interactivity", controller);
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
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.GET, "/interactivity/" + interactivityId + "/admin");
		request.setHeader("Action", "begin");
		connection.sendRequestAndAwaitResponse(request);
		// TODO make sure request was successful
	}
}
