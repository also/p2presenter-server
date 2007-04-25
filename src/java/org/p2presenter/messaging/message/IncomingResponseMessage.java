/* $Id$ */

package org.p2presenter.messaging.message;

import org.p2presenter.messaging.LocalConnection;

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
