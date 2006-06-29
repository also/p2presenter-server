package edu.uoregon.cs.p2presenter.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

public class P2PresenterServer {
	private Collection<ClientConnection> clientConnections = new LinkedList<ClientConnection>();
	
	public P2PresenterServer() {
		
	}
	
	public ClientConnection createClientConnection(Socket socket) throws IOException {
		ClientConnection clientConnection = new ClientConnection(socket, this);
		clientConnections.add(clientConnection);
		
		return clientConnection;
	}
	
	public void clientConnectionClosed(ClientConnection clientConnection) {
		clientConnections.remove(clientConnection);
	}
}
