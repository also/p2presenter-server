/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

import edu.uoregon.cs.p2presenter.message.DefaultMessageIdSource;
import edu.uoregon.cs.p2presenter.message.MessageIdSource;

public class ConnectionManager implements ConnectionListener, MessageIdSource {
	private Collection<LocalConnection> connections = new LinkedList<LocalConnection>();
	private int connectionId = 0;
	private MessageIdSource messageIdSource = DefaultMessageIdSource.newUniqueMessageIdSource("GLOBAL");
	
	private DefaultRequestHandlerMapping requestHandlerMapping = new DefaultRequestHandlerMapping();
	
	public ConnectionManager() {
	}
	
	public DefaultRequestHandlerMapping getRequestHandlerMapping() {
		return requestHandlerMapping;
	}
	
	public LocalConnection createConnection(Socket socket) throws IOException {
		LocalConnection connection;
		synchronized (connections) {
			connection = new LocalConnection(socket, connectionId);
		}
		connection.addConnectionListener(this);
		connection.getRequestHandlerMapping().setParent(requestHandlerMapping);
		connections.add(connection);
		connectionCreatedInternal(connection);
		return connection;
	}
	
	protected void connectionCreatedInternal(LocalConnection connection) {}
	
	public void connectionClosed(LocalConnection connection) {
		connections.remove(connection);
		connectionClosedInternal(connection);
	}
	
	protected void connectionClosedInternal(LocalConnection connection) {}

	public String generateMessageId() {
		return messageIdSource.generateMessageId();
	}
}
