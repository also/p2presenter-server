/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import edu.uoregon.cs.p2presenter.message.Message;
import edu.uoregon.cs.p2presenter.message.AbstractMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.ParseException;

public class Connection extends Thread implements Closeable {
	public static final String VERSION = "0.1";
	private Socket socket;
	
	private ConnectionManager manager;
	
	private BufferedOutputStream out;
	private PushbackInputStream in;

	private Interpreter interpreter = new Interpreter();
	
	private HashMap<String, ResponseMessage> responses = new HashMap<String, ResponseMessage>();
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
		
		Message incomingMessage;
		OutgoingResponseMessage responseMessage;
		
		try {
			for (;;) {
				incomingMessage = read();
				
				if (incomingMessage == null) {
					close();
					// TODO more stuff
					return;
				}
				String responseContent = null;
				if (incomingMessage.isRequest()) {
					status = 200;
					try {
						if (incomingMessage.hasContent()) {
							Object result = interpreter.eval(incomingMessage.getContentAsString());
							if (result != null) {
								responseContent = result.toString();
							}
						}
					}
					catch (ParseException ex) {
						status = 400;
						responseContent = ex.getMessage();
					}
					catch (EvalError ex) {
						status = 500;
						responseContent = ex.getMessage();
					}
					finally {
						responseMessage = new OutgoingResponseMessage(status, (RequestMessage) incomingMessage);
						if (responseContent != null) {
							responseMessage.setContent(responseContent);
						}
						write(responseMessage);
					}
				}
				else {
					responseRecieved((ResponseMessage) incomingMessage);
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
	
	private void responseRecieved(ResponseMessage message) {
		responsesLock.lock();
		responses.put(message.getInResponseTo(), message);
		responseReceived.signalAll();
		responsesLock.unlock();
	}
	
	public ResponseMessage awaitResponse(RequestMessage message) throws InterruptedException {
		if(!running) {
			throw new IllegalStateException("Not running");
		}
		
		ResponseMessage result;
		responsesLock.lock();
		result = responses.remove(message.getMessageId());
		while (result == null) {
			responseReceived.await();
			result = responses.remove(message.getMessageId());
		}
		responsesLock.unlock();

		return result;
	}
	
	public Message read() throws IOException {
		synchronized (in) {
			return AbstractMessage.read(in);
		}
	}
	
	public void write(OutgoingMessage message) throws IOException {
		synchronized (out) {
			message.write(out);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, String variableName) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new ConnectionInvocationHandler(this, interfaceClass, variableName));
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
