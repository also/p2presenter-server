package edu.uoregon.cs.p2presenter.server;

import java.io.IOException;
import java.net.ServerSocket;

import edu.uoregon.cs.p2presenter.ConnectionManager;

public class P2PresenterServerPortListener implements Runnable {
	private ServerSocket serverSocket;
	private ConnectionManager server;
	
	public P2PresenterServerPortListener(int portNumber, ConnectionManager server) throws IOException {
		serverSocket = new ServerSocket(portNumber);
		this.server = server;
	}
	
	public void run() {
		while(true) {
			try {
				server.createConnection(serverSocket.accept()).start();
			}
			catch (IOException e) {
				// FIXME do stuff
			}
		}
	}
}
