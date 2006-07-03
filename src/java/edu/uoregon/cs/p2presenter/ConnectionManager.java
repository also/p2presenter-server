package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

import edu.uoregon.cs.p2presenter.message.DefaultMessageIdSource;
import edu.uoregon.cs.p2presenter.message.MessageIdSource;

public class ConnectionManager implements MessageIdSource {
	private Collection<Connection> connections = new LinkedList<Connection>();
	
	private MessageIdSource messageIdSource = DefaultMessageIdSource.newUniqueMessageIdSource("GLOBAL");
	
	public ConnectionManager() {
		
	}
	
	public Connection createConnection(Socket socket) throws IOException {
		Connection connection = new Connection(socket, this);
		connections.add(connection);
		
		return connection;
	}
	
	public void connectionClosed(Connection connection) {
		connections.remove(connection);
	}

	public String generateMessageId() {
		return messageIdSource.generateMessageId();
	}
}
