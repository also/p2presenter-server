package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;


public class ConnectionManager {
	private Collection<Connection> connections = new LinkedList<Connection>();
	
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
}
