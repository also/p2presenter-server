/* $Id$ */

package edu.uoregon.cs.p2presenter.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionManager;
import edu.uoregon.cs.p2presenter.jsh.JshClient;

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
		try {
			Client client = new Client("localhost", 9000);
			
			client.run();
		}
		catch (ConnectException ex) {
			System.out.println("Couldn't connect to server");
		}
	}
	
	public void run() {
		connection.start();
		
		BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));

		JshClient jsh = new JshClient(connection);
		
		String line;
		StringBuilder commandBuilder = new StringBuilder();
		try {
			while ((line = sysIn.readLine()) != null) {
				if ("send".equals(line)) {
					try {
						System.out.println(jsh.eval(commandBuilder.toString()));
					}
					catch(Exception ex) {
						System.out.println("Remote evaluation failed: " + ex.getMessage());
					}
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
