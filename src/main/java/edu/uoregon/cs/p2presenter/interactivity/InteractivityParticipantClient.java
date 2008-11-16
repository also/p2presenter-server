/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import java.awt.Container;

import org.ry1.json.JsonObject;

import com.ryanberdeen.djava.RemoteObjectReference;
import com.ryanberdeen.djava.postal.InvocationRequestHandler;
import com.ryanberdeen.djava.postal.PostalDJavaConnection;
import com.ryanberdeen.postal.LocalConnection;
import com.ryanberdeen.postal.handler.UriPatternRequestMatcher;
import com.ryanberdeen.postal.message.IncomingResponseMessage;
import com.ryanberdeen.postal.message.OutgoingRequestMessage;

public class InteractivityParticipantClient {
	private PostalDJavaConnection dJavaConnection;
	private Container view;
	
	private Object model;
	
	//  TODO narrow exceptions
	@SuppressWarnings("unchecked")
	public InteractivityParticipantClient(LocalConnection connection, int interactivityId) throws Exception {
		OutgoingRequestMessage getInteractivityRequest = new OutgoingRequestMessage(connection, "/interactivity/" + interactivityId + "/get");
		IncomingResponseMessage response = connection.sendRequestAndAwaitResponse(getInteractivityRequest);
		if (response.getStatus() != 200) {
			// TODO exception type
			throw new Exception("Could not get interactivity.");
		}
		
		JsonObject responseObject = JsonObject.valueOf(response.getContentAsString());
		Class<? extends Container> participantViewClass = (Class<? extends Container>) Class.forName(responseObject.get("participantViewClassName").toString());
		view = participantViewClass.newInstance();
		
		Class<?> modelClass = Class.forName(responseObject.get("participantModelInterfaceClassName").toString());
		
		OutgoingRequestMessage joinInteractivityRequest = new OutgoingRequestMessage(connection, "/interactivity/" + interactivityId + "/join");
		response = connection.sendRequestAndAwaitResponse(joinInteractivityRequest);
		if (response.getStatus() != 200) {
			// TODO exception type
			throw new Exception("Could not join interactivity: " + response.getContentAsString());
		}
		responseObject = JsonObject.valueOf(response.getContentAsString());
	
		int modelProxyId = ((Number) responseObject.get("participantModelProxyId")).intValue();
		dJavaConnection = new PostalDJavaConnection(connection, "/interactivity/" + interactivityId + "/controller", true);
		InvocationRequestHandler invoker = new InvocationRequestHandler();
		connection.getRequestHandlerMapping().mapHandler(new UriPatternRequestMatcher("/interactivity/(\\d+)/controller", "interactivityId"), invoker);
		
		model = dJavaConnection.proxy(modelClass, modelProxyId);
		
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
