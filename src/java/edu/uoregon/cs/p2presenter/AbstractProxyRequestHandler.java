/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;

import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

/** Handles a request by sending it to another connection.
 * @author rberdeen
 *
 */
public abstract class AbstractProxyRequestHandler implements RequestHandler {
	private ConnectionManager connectionManager;
	
	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
	
	protected abstract LocalConnection getTargetConnection(IncomingRequestMessage request);
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage incomingRequest) throws IOException {
		LocalConnection target;
		
		String targetConnectionId = incomingRequest.getHeader("Target-Connection-Id");
		String proxiedConnectionId = null;
		if (targetConnectionId != null) {
			target = connectionManager.getConnection(targetConnectionId);
		}
		else {
			target = getTargetConnection(incomingRequest);
			proxiedConnectionId = incomingRequest.getLocalConnection().getConnectionId();
		}
		
		sendProxiedRequest(target, incomingRequest, proxiedConnectionId);
		
		// the response will be sent by the proxy response handler
		return null;
	}
	
	public static void sendProxiedRequest(LocalConnection target, IncomingRequestMessage incomingRequest, String proxiedConnectionId) throws IOException {
		OutgoingRequestMessage outgoingRequest = new OutgoingRequestMessage(target, incomingRequest);
		if (proxiedConnectionId != null) {
			outgoingRequest.setHeader("Proxied-Connection-Id", proxiedConnectionId);
		}
		target.sendRequest(outgoingRequest, new ProxyResponseHandler(incomingRequest));
	}
	
	/** Handles a response by sending it to the original requestor.
	 * @author rberdeen
	 *
	 */
	private static class ProxyResponseHandler implements ResponseHandler<Object> {
		private IncomingRequestMessage request;
		
		public ProxyResponseHandler(IncomingRequestMessage request) {
			this.request = request;
		}
		
		public Object handleResponse(IncomingResponseMessage targetResponse) throws Exception {
			OutgoingResponseMessage response = new OutgoingResponseMessage(request, targetResponse);
			
			request.getConnection().sendResponse(response);
			
			return null;
		}
	}
}
