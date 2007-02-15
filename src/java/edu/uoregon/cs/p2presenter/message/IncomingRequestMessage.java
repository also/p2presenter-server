/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.util.HashMap;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.ProxiedConnection;

public class IncomingRequestMessage extends AbstractRequestMessage implements IncomingRequestHeaders, IncomingMessage {
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private LocalConnection localConnection;
	
	protected IncomingRequestMessage(LocalConnection localConnection, RequestType requestType, String uri) {
		super(requestType, uri);
		this.localConnection = localConnection;
	}
	
	public final Connection getConnection() {
		String proxiedConnectionId = getHeader("Proxied-Connection-Id");
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
