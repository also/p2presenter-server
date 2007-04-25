/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import java.awt.Container;

import org.p2presenter.messaging.LocalConnection;
import org.p2presenter.messaging.handler.UriPatternRequestMatcher;
import org.p2presenter.messaging.message.IncomingResponseMessage;
import org.p2presenter.messaging.message.OutgoingRequestMessage;
import org.p2presenter.messaging.message.RequestHeaders.RequestType;
import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.remoting.InvocationRequestHandler;
import edu.uoregon.cs.p2presenter.remoting.RemoteInvocationConnection;
import edu.uoregon.cs.p2presenter.remoting.RemoteObjectReference;

public class InteractivityParticipantClient {
	private RemoteInvocationConnection remoteInvocationConnection;
	private Container view;
	
	private Object model;
	
	//  TODO narrow exceptions
	@SuppressWarnings("unchecked")
	public InteractivityParticipantClient(LocalConnection connection, int interactivityId) throws Exception {
		OutgoingRequestMessage getInteractivityRequest = new OutgoingRequestMessage(connection, RequestType.GET, "/interactivity/" + interactivityId + "/get");
		IncomingResponseMessage response = connection.sendRequestAndAwaitResponse(getInteractivityRequest);
		if (response.getStatus() != 200) {
			// TODO exception type
			throw new Exception("Could not get interactivity.");
		}
		
		JsonObject responseObject = JsonObject.valueOf(response.getContentAsString());
		Class<? extends Container> participantViewClass = (Class<? extends Container>) Class.forName(responseObject.get("participantViewClassName").toString());
		view = participantViewClass.newInstance();
		
		Class<?> modelClass = Class.forName(responseObject.get("participantModelInterfaceClassName").toString());
		
		OutgoingRequestMessage joinInteractivityRequest = new OutgoingRequestMessage(connection, RequestType.GET, "/interactivity/" + interactivityId + "/join");
		response = connection.sendRequestAndAwaitResponse(joinInteractivityRequest);
		if (response.getStatus() != 200) {
			// TODO exception type
			throw new Exception("Could not join interactivity: " + response.getContentAsString());
		}
		responseObject = JsonObject.valueOf(response.getContentAsString());
	
		int modelProxyId = ((Number) responseObject.get("participantModelProxyId")).intValue();
		remoteInvocationConnection = new RemoteInvocationConnection(connection, "/interactivity/" + interactivityId + "/controller", true);
		InvocationRequestHandler invoker = new InvocationRequestHandler();
		connection.getRequestHandlerMapping().mapHandler(new UriPatternRequestMatcher("/interactivity/(\\d+)/controller", "interactivityId"), invoker);
		
		model = remoteInvocationConnection.proxy(modelClass, new RemoteObjectReference(modelProxyId));
		
		if (view instanceof InteractivityClientComponent) {
			((InteractivityClientComponent) view).setModel(model);
		}
	}
	
	public Object getModel() {
		return model;
	}
	
	public Container getView() {
		return view;
	}
}
