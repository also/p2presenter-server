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

import edu.uoregon.cs.p2presenter.jsh.RemoteJshInvocationHandler;
import edu.uoregon.cs.p2presenter.message.AbstractMessage;
import edu.uoregon.cs.p2presenter.message.DefaultMessageIdSource;
import edu.uoregon.cs.p2presenter.message.Message;
import edu.uoregon.cs.p2presenter.message.MessageIdSource;
import edu.uoregon.cs.p2presenter.message.OutgoingMessage;
import edu.uoregon.cs.p2presenter.message.RequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;

public class Connection extends Thread implements MessageIdSource, Closeable {
	public static final String PROTOCOL = "P2PR";
	public static final String VERSION = "0.1";
	public static final String PROTOCOL_VERSION = PROTOCOL + '/' + VERSION;
	private Socket socket;
	
	private ConnectionManager manager;
	
	private BufferedOutputStream out;
	private PushbackInputStream in;

	private RequestHandlerMapping requestHandlerMapping = new DefaultConnectionRequestHandlerMapping();
	
	private HashMap<String, ResponseMessage> responses = new HashMap<String, ResponseMessage>();
	private ReentrantLock responsesLock = new ReentrantLock(true);
	private Condition responseReceived = responsesLock.newCondition();
	
	private MessageIdSource messageIdSource = new DefaultMessageIdSource();
	
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
		
		Message incomingMessage;
		
		try {
			for (;;) {
				incomingMessage = recieve();
				
				if (incomingMessage == null) {
					close();
					// TODO more stuff
					return;
				}

				if (incomingMessage.isRequest()) {
					send(requestHandlerMapping.getHandler(incomingMessage).processRequest((RequestMessage) incomingMessage));
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
	
	private Message recieve() throws IOException {
		synchronized (in) {
			return AbstractMessage.read(in);
		}
	}
	
	public void send(OutgoingMessage message) throws IOException {
		synchronized (out) {
			message.write(out);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(Class<T> interfaceClass, String variableName) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new RemoteJshInvocationHandler(this, interfaceClass, variableName));
	}
	
	/** Return a message id for a message.
	 * The message id is guaranteed to be unique for the duration of the connection.
	 */
	public String generateMessageId() {
		return messageIdSource.generateMessageId();
	}
	
	public void close() throws IOException {
		// TODO
		socket.close();
		manager.connectionClosed(this);
	}
}
