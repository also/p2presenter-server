package edu.uoregon.cs.p2presenter;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import bsh.EvalError;
import bsh.Interpreter;

public class Connection extends Thread implements Closeable {
	private Socket socket;
	
	private ConnectionManager manager;
	
	private OutgoingMessageStream out;
	private IncomingMessageStream in;

	private Interpreter interpreter = new Interpreter();

	private SynchronousQueue<Message> response = new SynchronousQueue<Message>(true);
	
	private int messageId = 1;
	
	public Connection(Socket socket, ConnectionManager manager) throws IOException {
		this.socket = socket;
		
		this.manager = manager;
		
		out = new OutgoingMessageStream(socket.getOutputStream());
		in = new IncomingMessageStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		int status;
		
		Message message;
		
		MessageSender sender = new MessageSender(this);
		
		try {
			for (;;) {
				message = in.read();
				
				if(message.hasContent()) {
					status = 200;
					try {
						interpreter.eval(message.getContentAsString());
					}
					catch (EvalError ex) {
						// TODO
						ex.printStackTrace();
						status = 500;
					}
					finally {
						sender.setHeader("Status", String.valueOf(status));
						sender.send();
					}
				}
				else {
					// TODO in the future, messages will be sent asynchronously; not all empty messages will be responses (that's the whole purpose of this thread)
					for (;;) {
						try {
							response.put(message);
							break;
						}
						catch (InterruptedException ex) {
							// FIXME
						}
					}
				}
			}
		}
		catch (IOException ex) {
			// FIXME
			ex.printStackTrace();
		}
	}
	
	public Message awaitResponse() throws InterruptedException {
		return response.take();
	}

	public IncomingMessageStream getIn() {
		return in;
	}

	public OutgoingMessageStream getOut() {
		return out;
	}
	
	public Interpreter getInterpreter() {
		return interpreter;
	}
	
	public String generateMessageId() {
		return String.valueOf(messageId++);
	}
	
	public void close() throws IOException {
		socket.close();
		manager.connectionClosed(this);
	}
}
