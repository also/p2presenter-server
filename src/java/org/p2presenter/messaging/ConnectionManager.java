/* $Id$ */

package org.p2presenter.messaging;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import org.p2presenter.messaging.handler.DefaultRequestHandlerMapping;


public class ConnectionManager implements ConnectionLifecycleListener, IdGenerator {
	private HashMap<String, LocalConnection> connections = new HashMap<String, LocalConnection>();
	private IdGenerator idSource = DefaultIdGenerator.newUniqueIdGenerator("GLOBAL");
	
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
			connection = new LocalConnection(socket, idSource.generateId());
		}
		connection.addConnectionLifecycleListener(this);
		connection.getRequestHandlerMapping().setParent(requestHandlerMapping);
		connections.put(connection.getConnectionId(), connection);
		connectionCreatedInternal(connection);
		return connection;
	}
	
	protected void connectionCreatedInternal(LocalConnection connection) {}
	
	public void connectionClosed(Connection connection) {
		connections.remove(connection.getConnectionId());
		connectionClosedInternal(connection);
	}
	
	protected void connectionClosedInternal(Connection connection) {}

	public String generateId() {
		return idSource.generateId();
	}
}
