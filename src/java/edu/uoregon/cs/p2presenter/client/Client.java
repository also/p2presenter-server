package edu.uoregon.cs.p2presenter.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionManager;
import edu.uoregon.cs.p2presenter.MessageSender;

public class Client implements Runnable {
	private Connection connection;
	
	private Socket socket;
	
	public Client(String host, int port) throws IOException {

		socket = new Socket(host, port);

		connection = new ConnectionManager().createConnection(socket);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new Client("localhost", 9000).run();
	}
	
	public void run() {
		connection.start();
		MessageSender sender = new MessageSender(connection);
		BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
		for(;;) {
			String line;
			StringBuilder commandBuilder = new StringBuilder();
			try {
				while ((line = sysIn.readLine()) != null) {
					if ("send".equals(line)) {
						sender.setContent(commandBuilder.toString());
						
						try {
							System.out.println("Status: " + sender.sendAndAwaitResponse().getHeader("Status"));
						}
						catch(InterruptedException ex) {}
						commandBuilder = new StringBuilder();
					}
					else {
						if (commandBuilder.length() != 0) {
							commandBuilder.append('\n');
						}
						commandBuilder.append(line);
					}
				}
			}
			catch(IOException ex) {
				
			}
		}
	}
}
