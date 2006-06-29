package edu.uoregon.cs.p2presenter.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import edu.uoregon.cs.p2presenter.MessageSender;
import edu.uoregon.cs.p2presenter.OutgoingMessageStream;

public class Client implements Runnable {
	private BufferedReader sysIn;

	private OutgoingMessageStream out;
	private MessageSender sender;
	
	private Socket socket;
	
	public Client(String host, int port) throws IOException {

		socket = new Socket(host, port);

		sysIn = new BufferedReader(new InputStreamReader(System.in));
		
		out = new OutgoingMessageStream(socket.getOutputStream());
		sender = new MessageSender(out);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new Client("localhost", 9000).run();
	}
	
	public void run() {
		for(;;) {
			String line;
			StringBuilder commandBuilder = new StringBuilder();
			try {
				while ((line = sysIn.readLine()) != null) {
					if ("send".equals(line)) {
						sendCommand(commandBuilder.toString());
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
	
	private void sendCommand(String command) throws IOException {
		sender.setContent(command);
		sender.send();
	}
}
