package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

import edu.uoregon.cs.p2presenter.message.DefaultMessageIdSource;
import edu.uoregon.cs.p2presenter.message.MessageIdSource;

public class ConnectionManager implements ConnectionListener, MessageIdSource {
	private Collection<Connection> connections = new LinkedList<Connection>();
	
	private MessageIdSource messageIdSource = DefaultMessageIdSource.newUniqueMessageIdSource("GLOBAL");
	
	private DefaultRequestHandlerMapping requestHandlerMapping = new DefaultRequestHandlerMapping();
	
	public ConnectionManager() {
	}
	
	public DefaultRequestHandlerMapping getRequestHandlerMapping() {
		return requestHandlerMapping;
	}
	
	public Connection createConnection(Socket socket) throws IOException {
		Connection connection = new Connection(socket);
		connection.addConnectionListener(this);
		connection.getRequestHandlerMapping().setParent(requestHandlerMapping);
		connections.add(connection);
		connectionCreatedInternal(connection);
		return connection;
	}
	
	protected void connectionCreatedInternal(Connection connection) {}
	
	public void connectionClosed(Connection connection) {
		connections.remove(connection);
		connectionClosedInternal(connection);
	}
	
	protected void connectionClosedInternal(Connection connection) {}

	public String generateMessageId() {
		return messageIdSource.generateMessageId();
	}
}
