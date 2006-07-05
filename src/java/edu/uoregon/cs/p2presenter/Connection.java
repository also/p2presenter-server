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
import edu.uoregon.cs.p2presenter.message.IncomingMessage;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
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
	
	private HashMap<String, IncomingResponseMessage> responses = new HashMap<String, IncomingResponseMessage>();
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
		
		IncomingMessage incomingMessage;
		
		try {
			for (;;) {
				incomingMessage = recieve();
				
				if (incomingMessage == null) {
					close();
					// TODO more stuff
					return;
				}

				if (incomingMessage.isRequest()) {
					send(requestHandlerMapping.getHandler(incomingMessage).processRequest((IncomingRequestMessage) incomingMessage));
				}
				else {
					responseReceived((IncomingResponseMessage) incomingMessage);
				}
			}
		}
		catch (IOException ex) {
			// exceptions are handled by send and receive
		}
		finally {
			running = false;
		}
	}
	
	private void responseReceived(IncomingResponseMessage message) {
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
	
	private IncomingMessage recieve() throws IOException {
		synchronized (in) {
			try {
				return AbstractMessage.read(this, in);
			}
			catch (IOException ex) {
				handleException(ex);
				throw ex;
			}
		}
	}
	
	public void send(OutgoingMessage message) throws IOException {
		synchronized (out) {
			try {
				message.write(out);
			}
			catch (IOException ex) {
				handleException(ex);
				throw ex;
			}
		}
	}
	
	private void handleException(Exception exception) {
		try {
			close();
		}
		catch (IOException ex) {
			// TODO log
		}
	}
	
	// TODO this doesn't belong here
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
		try {
			socket.close();
		}
		finally {
			manager.connectionClosed(this);
		}
	}
}
