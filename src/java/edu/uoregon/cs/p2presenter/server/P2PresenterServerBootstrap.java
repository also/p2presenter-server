package edu.uoregon.cs.p2presenter.server;

import java.io.IOException;

public class P2PresenterServerBootstrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ConnectionManager server = new ConnectionManager();
		P2PresenterServerPortListener portListener = new P2PresenterServerPortListener(9000, server);
		portListener.run();
	}

}
