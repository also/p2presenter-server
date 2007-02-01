/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import java.awt.Container;

import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;
import edu.uoregon.cs.p2presenter.remoting.RemoteInvocationConnection;
import edu.uoregon.cs.p2presenter.remoting.RemoteProxyReference;

public class InteractivityParticipantClient {
	private RemoteInvocationConnection remoteInvocationConnection;
	private Container view;
	
	private Object model;
	
	//  TODO narrow exceptions
	@SuppressWarnings("unchecked")
	public InteractivityParticipantClient(Connection connection, int interactivityId) throws Exception {
		OutgoingRequestMessage joinInteractivityRequest = new OutgoingRequestMessage(connection, RequestType.GET, "/interactivity/" + interactivityId + "/join");
		IncomingResponseMessage response = connection.sendRequestAndAwaitResponse(joinInteractivityRequest);
		if (response.getStatus() == 200) {
			JsonObject responseObject = JsonObject.valueOf(response.getContentAsString());
			Class<? extends Container> participantViewClass = (Class<? extends Container>) Class.forName(responseObject.get("participantViewClassName").toString());
			view = participantViewClass.newInstance();
			
			Class<?> modelClass = Class.forName(responseObject.get("participantModelInterfaceClassName").toString());
			int modelProxyId = ((Number) responseObject.get("participantModelProxyId")).intValue();
			remoteInvocationConnection = new RemoteInvocationConnection(connection, "/interactivity/" + interactivityId + "/controller");
			
			model = remoteInvocationConnection.proxy(modelClass, new RemoteProxyReference(modelProxyId));
			
			if (view instanceof InteractivityClientComponent) {
				((InteractivityClientComponent) view).setModel(model);
			}
		}
		else {
			// TODO exception type
			throw new Exception();
		}
	}
	
	public Object getModel() {
		return model;
	}
	
	public Container getView() {
		return view;
	}
}
