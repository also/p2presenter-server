/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.LocalConnection;

public class IncomingResponseMessage extends AbstractResponseMessage implements IncomingMessage {
	private LocalConnection connection;
	
	protected IncomingResponseMessage(LocalConnection connection, int status, String reason) {
		super(status, reason);
		this.connection = connection;
	}

	public final LocalConnection getLocalConnection() {
		return connection;
	}
}
