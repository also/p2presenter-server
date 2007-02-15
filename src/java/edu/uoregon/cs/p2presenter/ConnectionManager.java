/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import edu.uoregon.cs.p2presenter.message.DefaultMessageIdSource;
import edu.uoregon.cs.p2presenter.message.MessageIdSource;

public class ConnectionManager implements ConnectionListener, MessageIdSource {
	private HashMap<String, LocalConnection> connections = new HashMap<String, LocalConnection>();
	private int connectionId = 0;
	private MessageIdSource messageIdSource = DefaultMessageIdSource.newUniqueMessageIdSource("GLOBAL");
	
	private DefaultRequestHandlerMapping requestHandlerMapping = new DefaultRequestHandlerMapping();
	
	/** Returns the connection with the specified ID.
	 */
	public LocalConnection getConnection(String connectionId) {
		return connections.get(connectionId);
	}
	
	public DefaultRequestHandlerMapping getRequestHandlerMapping() {
		return requestHandlerMapping;
	}
	
	public LocalConnection createConnection(Socket socket) throws IOException {
		LocalConnection connection;
		synchronized (connections) {
			connection = new LocalConnection(socket, String.valueOf(connectionId));
		}
		connection.addConnectionListener(this);
		connection.getRequestHandlerMapping().setParent(requestHandlerMapping);
		connections.put(String.valueOf(connectionId++), connection);
		connectionCreatedInternal(connection);
		return connection;
	}
	
	protected void connectionCreatedInternal(LocalConnection connection) {}
	
	public void connectionClosed(Connection connection) {
		connections.remove(connection.getConnectionId());
		connectionClosedInternal(connection);
	}
	
	protected void connectionClosedInternal(Connection connection) {}

	public String generateMessageId() {
		return messageIdSource.generateMessageId();
	}
}
