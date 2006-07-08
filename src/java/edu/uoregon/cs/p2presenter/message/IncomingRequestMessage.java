/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public class IncomingRequestMessage extends AbstractRequestMessage implements IncomingHeaders {
	private Connection connection;
	
	protected IncomingRequestMessage(Connection connection, RequestType requestType, String url) {
		super(requestType, url);
		this.connection = connection;
	}

	public final Connection getConnection() {
		return connection;
	}

}
