/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import java.awt.Container;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;
import edu.uoregon.cs.p2presenter.remoting.RemoteInvocationConnection;
import edu.uoregon.cs.p2presenter.remoting.RemoteProxyReference;

public class InteractivityClient {
	public static final String CLIENT_VIEW_CLASS_NAME_HEADER_NAME = "Client-Container-Class-Name";
	public static final String MODEL_CLASS_NAME_HEADER_NAME = "Model-Class-Name";
	public static final String MODEL_PROXY_ID_HEADER_NAME = "Model-Proxy-Id";
	
	private RemoteInvocationConnection remoteInvocationConnection;
	private Container view;
	
	private Object model;
	
	//  TODO narrow exceptions
	@SuppressWarnings("unchecked")
	public InteractivityClient(Connection connection, int interactivityId) throws Exception {
		OutgoingRequestMessage joinInteractivityRequest = new OutgoingRequestMessage(connection, RequestType.GET, InteractivityRequestMatcher.URI_PREFIX + interactivityId + "/join");
		IncomingResponseMessage response = connection.sendRequestAndAwaitResponse(joinInteractivityRequest);
		
		Class<? extends Container> clientViewClass = (Class<? extends Container>) Class.forName(response.getHeader(CLIENT_VIEW_CLASS_NAME_HEADER_NAME));
		view = clientViewClass.newInstance();
		
		Class<?> modelClass = Class.forName(response.getHeader(MODEL_CLASS_NAME_HEADER_NAME));
		Integer modelProxyId = new Integer(response.getHeader(MODEL_PROXY_ID_HEADER_NAME));
		remoteInvocationConnection = new RemoteInvocationConnection(connection, InteractivityRequestMatcher.URI_PREFIX + interactivityId + "/controller");
		
		model = remoteInvocationConnection.proxy(modelClass, new RemoteProxyReference(modelProxyId));
		
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
