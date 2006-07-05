/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public class IncomingRequestMessage extends AbstractRequestMessage implements IncomingMessage {
	private Connection connection;
	
	public IncomingRequestMessage(Connection connection, RequestType requestType, String url) {
		super(requestType, url);
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

}
