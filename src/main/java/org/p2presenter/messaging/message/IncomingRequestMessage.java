/* $Id$ */

package org.p2presenter.messaging.message;

import java.util.HashMap;

import org.p2presenter.messaging.Connection;
import org.p2presenter.messaging.LocalConnection;
import org.p2presenter.messaging.ProxiedConnection;


public class IncomingRequestMessage extends AbstractRequestMessage implements IncomingRequestHeaders, IncomingMessage {
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private LocalConnection localConnection;
	
	protected IncomingRequestMessage(LocalConnection localConnection, String requestType, String uri) {
		super(requestType, uri);
		this.localConnection = localConnection;
	}
	
	public final Connection getConnection() {
		String proxiedConnectionId = getHeader(ProxiedConnection.PROXIED_CONNECTION_ID_HEADER_NAME);
		if (proxiedConnectionId != null) {
			return ProxiedConnection.getProxiedConnection(localConnection, proxiedConnectionId);
		}
		else {
			return localConnection;
		}
	}

	public final LocalConnection getLocalConnection() {
		return localConnection;
	}
	
	public Object getAttribute(String name) {
		return attributes != null ? attributes.get(name) : null;
	}

	public void setAttribute(String name, Object value) {
		if (attributes == null) {
			attributes = new HashMap<String, Object>();
		}
		attributes.put(name, value);
	}
}