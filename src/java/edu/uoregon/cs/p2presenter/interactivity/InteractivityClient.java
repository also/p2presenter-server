/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;
import edu.uoregon.cs.p2presenter.remoting.RemoteInvocationConnection;

public class InteractivityClient {
	public static final String MODEL_CLASS_HEADER_NAME = "Model-Class";
	public static final String MODEL_PROXY_ID_HEADER_NAME = "Model-Proxy-Id";
	
	private RemoteInvocationConnection remoteInvocationConnection;
	
	private Object model;
	
	//  TODO narrow exceptions
	public InteractivityClient(Connection connection, int interactivityId) throws Exception {
		OutgoingRequestMessage joinInteractivityRequest = new OutgoingRequestMessage(connection, RequestType.GET, InteractivityRequestMatcher.URI_PREFIX + interactivityId + "/join");
		IncomingResponseMessage response = connection.sendRequestAndAwaitResponse(joinInteractivityRequest);
		Class<?> modelClass = Class.forName(response.getHeader(MODEL_CLASS_HEADER_NAME));
		Integer modelProxyId = new Integer(response.getHeader(MODEL_PROXY_ID_HEADER_NAME));
		remoteInvocationConnection = new RemoteInvocationConnection(connection, InteractivityRequestMatcher.URI_PREFIX + interactivityId + "/controller");
		
		model = remoteInvocationConnection.proxy(modelClass, modelProxyId);
	}
	
	@SuppressWarnings("unchecked")
	public Object getModel() {
		return model;
	}

}
