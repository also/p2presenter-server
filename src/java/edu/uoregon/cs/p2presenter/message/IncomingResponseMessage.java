/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public class IncomingResponseMessage extends AbstractResponseMessage implements IncomingMessage {
	private Connection connection;
	
	public IncomingResponseMessage(Connection connection, int status, String reason) {
		super(status, reason);
		this.connection = connection;
	}

	protected IncomingResponseMessage(Connection connection, int status) {
		super(status);
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}


}
