package edu.uoregon.cs.p2presenter;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

import bsh.EvalError;
import bsh.Interpreter;

public class Connection extends Thread implements Closeable {
	private Socket socket;
	
	private ConnectionManager manager;
	
	private BufferedOutputStream out;
	private PushbackInputStream in;

	private Interpreter interpreter = new Interpreter();

	private SynchronousQueue<Message> response = new SynchronousQueue<Message>(true);
	
	private Integer messageId = 1;
	
	public Connection(Socket socket, ConnectionManager manager) throws IOException {
		this.socket = socket;
		
		this.manager = manager;
		
		out = new BufferedOutputStream(socket.getOutputStream());
		in = new PushbackInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		int status;
		
		Message incomingMessage;
		OutgoingMessage outgoingMessage;
		
		try {
			for (;;) {
				incomingMessage = read();
				if(incomingMessage.hasContent()) {
					status = 200;
					try {
						interpreter.eval(incomingMessage.getContentAsString());
					}
					catch (EvalError ex) {
						// TODO
						ex.printStackTrace();
						status = 500;
					}
					finally {
						outgoingMessage = new OutgoingMessage(incomingMessage);
						outgoingMessage.setHeader("Status", String.valueOf(status));
						write(outgoingMessage);
					}
				}
				else {
					// TODO in the future, messages will be sent asynchronously; not all empty messages will be responses (that's the whole purpose of this thread)
					for (;;) {
						try {
							response.put(incomingMessage);
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
	
	public Message read() throws IOException {
		synchronized (in) {
			return Message.read(in);
		}
	}
	
	public void write(OutgoingMessage message) throws IOException {
		synchronized (out) {
			message.write(out);
		}
	}
	
	public Interpreter getInterpreter() {
		return interpreter;
	}
	
	/** Return a message id for a message.
	 * The message id is guaranteed to be unique for the duration of the connection.
	 */
	public String generateMessageId() {
		synchronized (messageId) {
			return String.valueOf(messageId++);
		}
	}
	
	public void close() throws IOException {
		socket.close();
		manager.connectionClosed(this);
	}
}
