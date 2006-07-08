/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public class IncomingResponseMessage extends AbstractResponseMessage implements IncomingMessage {
	private Connection connection;
	
	protected IncomingResponseMessage(Connection connection, int status, String reason) {
		super(status, reason);
		this.connection = connection;
	}

	public final Connection getConnection() {
		return connection;
	}


}
