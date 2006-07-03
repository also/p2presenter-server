package edu.uoregon.cs.p2presenter;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import bsh.EvalError;
import bsh.Interpreter;

public class Connection extends Thread implements Closeable {
	private Socket socket;
	
	private ConnectionManager manager;
	
	private BufferedOutputStream out;
	private PushbackInputStream in;

	private Interpreter interpreter = new Interpreter();
	
	private HashMap<String, MessageImpl> responses = new HashMap<String, MessageImpl>();
	private ReentrantLock responsesLock = new ReentrantLock(true);
	private Condition responseReceived = responsesLock.newCondition();
	
	private Integer messageId = 1;
	
	boolean running = false;
	
	public Connection(Socket socket, ConnectionManager manager) throws IOException {
		this.socket = socket;
		
		this.manager = manager;
		
		out = new BufferedOutputStream(socket.getOutputStream());
		in = new PushbackInputStream(socket.getInputStream());
	}
	
	@Override
	public void run() {
		running = true;
		int status;
		
		MessageImpl incomingMessage;
		OutgoingMessage outgoingMessage;
		
		try {
			for (;;) {
				incomingMessage = read();
				
				if(incomingMessage == null) {
					close();
					// TODO more stuff
					return;
				}
				
				if(incomingMessage.isResponse()) {
					responsesLock.lock();
					responses.put(incomingMessage.getInResponseTo(), incomingMessage);
					responseReceived.signalAll();
					responsesLock.unlock();
					
				}
				else {
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
						outgoingMessage.setStatus(status);
						write(outgoingMessage);
					}
				}
			}
		}
		catch (IOException ex) {
			// FIXME
			ex.printStackTrace();
		}
		finally {
			running = false;
		}
	}
	
	public MessageImpl awaitResponse(OutgoingMessage message) throws InterruptedException {
		if(!running) {
			throw new IllegalStateException("Not running");
		}
		
		MessageImpl result;
		responsesLock.lock();
		result = responses.remove(message.getMessageId());
		while (result == null) {
			responseReceived.await();
			result = responses.remove(message.getMessageId());
		}
		responsesLock.unlock();
		
		return result;
	}
	
	public MessageImpl read() throws IOException {
		synchronized (in) {
			return MessageImpl.read(in);
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
