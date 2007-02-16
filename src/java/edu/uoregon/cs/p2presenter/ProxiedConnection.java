/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

public class ProxiedConnection extends AbstractConnection {
	private static final String PROXIED_CONNECTION_ATTRIBUTE_NAME_PREFIX = ProxiedConnection.class.getName() + ".proxied_connection.";
	private LocalConnection localConnection;
	
	public ProxiedConnection(LocalConnection localConnection, String targetConnectionId) {
		super(targetConnectionId);
		this.localConnection = localConnection;
		localConnection.getRequestHandlerMapping().mapHandler("/connection/proxied/" + targetConnectionId + "/closed", new ProxiedConnectionClosedRequestHandler());
	}
	
	@Override
	public Object getAttribute(String key) {
		Object result = super.getAttribute(key);
		if (result == null) {
			result = localConnection.getAttribute(key);
		}
		return result;
	}

	public <V> Future<V> sendRequest(OutgoingRequestMessage request, ResponseHandler<V> responseHandler) throws IOException {
		setHeaders(request);
		onSend();
		return localConnection.sendRequest(request, responseHandler);
	}

	public void sendResponse(OutgoingResponseMessage response) throws IOException {
		setHeaders(response);
		onSend();
		localConnection.sendResponse(response);
	}
	
	private void setHeaders(OutgoingMessage message) {
		message.setHeader("Target-Connection-Id", getConnectionId().toString());
	}
	
	public static ProxiedConnection getProxiedConnection(LocalConnection localConnection, String proxiedConnectionId) {
		return (ProxiedConnection) localConnection.getAttribute(PROXIED_CONNECTION_ATTRIBUTE_NAME_PREFIX + proxiedConnectionId, new NewProxiedConnectionCallable(localConnection, proxiedConnectionId));
	}
	
	private static class NewProxiedConnectionCallable implements Callable<ProxiedConnection> {
		private LocalConnection localConnection;
		private String targetConnectionId;
		
		public NewProxiedConnectionCallable(LocalConnection localConnection, String targetConnectionId) {
			this.localConnection = localConnection;
			this.targetConnectionId = targetConnectionId;
		}

		public ProxiedConnection call() throws Exception {
			return new ProxiedConnection(localConnection, targetConnectionId);
		}
	}
	
	private class ProxiedConnectionClosedRequestHandler implements RequestHandler {
		public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
			onClose();
			localConnection.getRequestHandlerMapping().mapHandler("/connection/proxied/" + getConnectionId() + "/closed", null);
			return null;
		}
		
	}
}
