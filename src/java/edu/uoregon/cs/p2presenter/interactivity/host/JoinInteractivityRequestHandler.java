/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.host;

import org.ry1.json.JsonObject;

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
		
		ProxyCache proxyCache = ProxyCache.getProxyCache(request.getConnection(), proxyCacheUri, request.getProxiedConnectionId().toString());
		
		OutgoingResponseMessage response = new OutgoingResponseMessage(request);
		
		JsonObject responseObject = new JsonObject();
		responseObject.set("participantModelProxyId", proxyCache.getObjectDescriptor(model).getId());
		
		response.setContent(responseObject.toString());
		
		return response;
	}

}
