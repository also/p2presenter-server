package edu.uoregon.cs.p2presenter.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
	private BufferedReader sysIn;
	private PrintWriter outWriter;
	private OutputStream out;
	
	private Socket socket;
	
	public Client(String host, int port) throws IOException {

		socket = new Socket(host, port);

		sysIn = new BufferedReader(new InputStreamReader(System.in));
		out = socket.getOutputStream();
		outWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
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
		sendBytes(command.getBytes("UTF-8"));
	}
	
	private void sendBytes(byte[] bytes) throws IOException {
		outWriter.println("Content-Length: " + bytes.length);
		outWriter.println();
		outWriter.flush();
		out.write(bytes);
		outWriter.println();
		outWriter.flush();
	}
}
