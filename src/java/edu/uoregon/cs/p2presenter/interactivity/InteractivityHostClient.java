/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;
import edu.uoregon.cs.p2presenter.remoting.InvocationRequestHandler;

public class InteractivityHostClient {
	private InteractivityController<?> controller;
	private Connection connection;
	private int interactivityId;
	
	@SuppressWarnings("unchecked")
	public InteractivityHostClient(Connection connection, int interactivityId) throws Exception {
		this.connection = connection;
		this.interactivityId = interactivityId;
		
		OutgoingRequestMessage request = new OutgoingRequestMessage(connection, RequestType.GET, "/interactivity/" + interactivityId + "/admin");
		request.setHeader("Action", "get");
		// XXX make sure response is not null
		JsonObject responseObject = JsonObject.valueOf(connection.sendRequestAndAwaitResponse(request).getContentAsString());

		Class<InteractivityController> controllerClass = (Class<InteractivityController>) Class.forName(responseObject.get("hostControllerClassName").toString());
		controller = controllerClass.newInstance();
		
		InvocationRequestHandler invoker = new InvocationRequestHandler();
		connection.getRequestHandlerMapping().mapHandler(new InteractivityRequestMatcher("controller"), invoker);
		connection.setAttribute("interactivity", controller);
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
