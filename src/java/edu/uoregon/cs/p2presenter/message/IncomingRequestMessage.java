/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.util.HashMap;

import edu.uoregon.cs.p2presenter.LocalConnection;

public class IncomingRequestMessage extends AbstractRequestMessage implements IncomingRequestHeaders, IncomingMessage {
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private LocalConnection connection;
	
	protected IncomingRequestMessage(LocalConnection connection, RequestType requestType, String uri) {
		super(requestType, uri);
		this.connection = connection;
	}

	public final LocalConnection getConnection() {
		return connection;
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
	
	public Integer getProxiedConnectionId() {
		String resultString = getHeader(SpecialHeader.Proxied_Connection_Id);
		if (resultString != null) {
			return new Integer(resultString);
		}
		else {
			return null;
		}
	}
}
