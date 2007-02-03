/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.util.HashMap;

import edu.uoregon.cs.p2presenter.Connection;

public class IncomingRequestMessage extends AbstractRequestMessage implements IncomingRequestHeaders, IncomingMessage {
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private Connection connection;
	
	protected IncomingRequestMessage(Connection connection, RequestType requestType, String uri) {
		super(requestType, uri);
		this.connection = connection;
	}

	public final Connection getConnection() {
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
	
	public Integer getProxiedForConnectionId() {
		String resultString = getHeader(SpecialHeader.Proxied_For_Connection_Id);
		if (resultString != null) {
			return new Integer(resultString);
		}
		else {
			return null;
		}
	}
}
