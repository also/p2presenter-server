package edu.uoregon.cs.p2presenter.server;

import java.io.IOException;

import com.ryanberdeen.postal.ConnectionManager;
import com.ryanberdeen.postal.server.PostalServer;

public class P2PresenterServerPortListener {
	private int portNumber;
	private ConnectionManager connectionManager;
	private PostalServer postalServer;

	public P2PresenterServerPortListener() {}

	public P2PresenterServerPortListener(int portNumber, ConnectionManager connectionManager) throws IOException {
		this.portNumber = portNumber;
		this.connectionManager = connectionManager;
	}

	public void setPortNumber(int portNumber) throws IOException {
		this.portNumber = portNumber;
	}

	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public void init() throws Exception {
		postalServer = new PostalServer(portNumber, connectionManager);
		postalServer.start();
	}

	public void destroy() {
		postalServer.stop();
	}
}
