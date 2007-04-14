package edu.uoregon.cs.p2presenter.server;

import java.io.IOException;
import java.net.ServerSocket;

import edu.uoregon.cs.p2presenter.ConnectionManager;

public class P2PresenterServerPortListener implements Runnable {
	private int portNumber;
	private ServerSocket serverSocket;
	private ConnectionManager connectionManager;
	
	public P2PresenterServerPortListener() {}
	
	public P2PresenterServerPortListener(int portNumber, ConnectionManager connectionManager) throws IOException {
		this.portNumber = portNumber;
		this.connectionManager = connectionManager;
		serverSocket = new ServerSocket(portNumber);
	}
	
	public void setPortNumber(int portNumber) throws IOException {
		this.portNumber = portNumber;
	}
	
	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
	
	public void run() {
		
		while(!serverSocket.isClosed()) {
			try {
				connectionManager.createConnection(serverSocket.accept()).start();
			}
			catch (IOException e) {
				// FIXME do stuff
			}
		}
	}
	
	public void init() throws Exception {
		serverSocket = new ServerSocket(portNumber);
		new Thread(this).start();
	}
	
	public void destroy() {
		try {
			serverSocket.close();
		}
		catch (IOException ex) {
			// TODO
		}
	}
}
