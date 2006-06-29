package edu.uoregon.cs.p2presenter.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import bsh.EvalError;
import bsh.Interpreter;
import edu.uoregon.cs.p2presenter.IncomingMessageStream;
import edu.uoregon.cs.p2presenter.Message;

public class ClientConnection extends Thread {
	private Socket socket;
	private ConnectionManager server;
	private IncomingMessageStream in;
	private PrintWriter out;
	
	private Interpreter interpreter = new Interpreter();
	
	public ClientConnection(Socket socket, ConnectionManager server) {
		this.socket = socket;
		this.server = server;
	}
	
	public void run() {
		try {
			in = new IncomingMessageStream(socket.getInputStream());
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

			Message message;
			
			for(;;) {
				int status = 200;
				
				try {
					message = in.read();
					
					if(message.hasContent()) {
						interpreter.eval(new String(message.getContent(), "UTF-8"));
					}
				}
				catch (EvalError ex) {
					status = 500;
					ex.printStackTrace();
				}
				finally {
					out.println("Status: " + status);
					out.println();
				}
				
			}
		}
		catch(IOException ex) {
			// TODO
			return;
		}
		finally {
			server.clientConnectionClosed(this);
		}
	}
}
