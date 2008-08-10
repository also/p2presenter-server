/* $Id$ */

package org.p2presenter.messaging.handler;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.p2presenter.messaging.Connection;
import org.p2presenter.messaging.ConnectionLifecycleListener;
import org.p2presenter.messaging.LocalConnection;
import org.p2presenter.messaging.ProxiedConnection;
import org.p2presenter.messaging.ResponseHandler;
import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.IncomingResponseMessage;
import org.p2presenter.messaging.message.OutgoingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;


/** Handles a request by sending it to another connection.
 * @author rberdeen
 *
 */
public abstract class AbstractProxyRequestHandler implements RequestHandler {

	private static final String PROXIED_CONNECTIONS_ATTRIBUTE_NAME_PREFIX = AbstractProxyRequestHandler.class.getName() + ".proxiedConnections."; 
	
	protected abstract LocalConnection getTargetConnection(IncomingRequestMessage request);
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage incomingRequest) throws IOException {
		LocalConnection targetConnection;
		
		String targetConnectionId = incomingRequest.getHeader(ProxiedConnection.TARGET_CONNECTION_ID_HEADER_NAME);
		String proxiedConnectionId = null;
		if (targetConnectionId != null) {
			targetConnection = (LocalConnection) incomingRequest.getLocalConnection().getAttribute(PROXIED_CONNECTIONS_ATTRIBUTE_NAME_PREFIX + targetConnectionId);
		}
		else {
			targetConnection = getTargetConnection(incomingRequest);
			proxiedConnectionId = incomingRequest.getLocalConnection().getConnectionId();
			targetConnection.getAttribute(PROXIED_CONNECTIONS_ATTRIBUTE_NAME_PREFIX + proxiedConnectionId, new NewProxiedConnectionCallable(targetConnection, incomingRequest.getLocalConnection()));
		}
		
		sendProxiedRequest(targetConnection, incomingRequest, proxiedConnectionId);
		
		// the response will be sent by the proxy response handler
		return null;
	}
	
	public static void sendProxiedRequest(LocalConnection target, IncomingRequestMessage incomingRequest, String proxiedConnectionId) throws IOException {
		OutgoingRequestMessage outgoingRequest = new OutgoingRequestMessage(target, incomingRequest);
		if (proxiedConnectionId != null) {
			outgoingRequest.setHeader(ProxiedConnection.PROXIED_CONNECTION_ID_HEADER_NAME, proxiedConnectionId);
		}
		target.sendRequest(outgoingRequest, new ProxyResponseHandler(incomingRequest));
	}
	
	private static class NewProxiedConnectionCallable implements Callable<LocalConnection> {
		private LocalConnection targetConnection;
		private LocalConnection proxiedConnection;
		
		NewProxiedConnectionCallable(LocalConnection targetConnection, LocalConnection proxiedConnection) {
			this.targetConnection = targetConnection;
			this.proxiedConnection = proxiedConnection;
		}
		
		public LocalConnection call() throws Exception {
			proxiedConnection.addConnectionLifecycleListener(new ProxiedConnectionClosedListener(targetConnection, proxiedConnection));
			return proxiedConnection;
		}
	}
	
	private static class ProxiedConnectionClosedListener implements ConnectionLifecycleListener {
		private LocalConnection targetConnection;
		private LocalConnection proxiedConnection;
		
		ProxiedConnectionClosedListener(LocalConnection targetConnection, LocalConnection proxiedConnection) {
			this.targetConnection = targetConnection;
			this.proxiedConnection = proxiedConnection;
		}

		public void connectionClosed(Connection connection) {
			targetConnection.setAttribute(PROXIED_CONNECTIONS_ATTRIBUTE_NAME_PREFIX + proxiedConnection.getConnectionId(), null);
			
			try {
				targetConnection.sendRequest(new OutgoingRequestMessage(targetConnection, "/connection/proxied/" + proxiedConnection.getConnectionId() + "/closed"));
			}
			catch (IOException ex) {
				// TODO log
				// too bad
			}
		}
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