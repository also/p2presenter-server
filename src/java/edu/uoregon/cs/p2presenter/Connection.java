/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import edu.uoregon.cs.p2presenter.message.AbstractMessage;
import edu.uoregon.cs.p2presenter.message.DefaultMessageIdSource;
import edu.uoregon.cs.p2presenter.message.IncomingHeaders;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.MessageIdSource;
import edu.uoregon.cs.p2presenter.message.OutgoingHeaders;
import edu.uoregon.cs.p2presenter.message.OutgoingMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders;
import edu.uoregon.cs.p2presenter.message.RequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;

public class Connection extends Thread implements MessageIdSource, Closeable {
	public static final String PROTOCOL = "P2PR";
	public static final String VERSION = "0.1";
	public static final String PROTOCOL_VERSION = PROTOCOL + '/' + VERSION;
	private Socket socket;
	
	private ConnectionManager manager;
	
	private BufferedOutputStream out;
	private ReentrantLock outLock = new ReentrantLock();
	private PushbackInputStream in;

	private RequestHandlerMapping requestHandlerMapping = new DefaultConnectionRequestHandlerMapping();
	
	private HashMap<String, IncomingResponseMessage> responses = new HashMap<String, IncomingResponseMessage>();
	private ReentrantLock responsesLock = new ReentrantLock();
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
		
		IncomingHeaders incomingMessage;
		
		try {
			for (;;) {
				incomingMessage = recieve();
				
				if (incomingMessage == null) {
					close();
					// TODO more stuff
					return;
				}

				if (incomingMessage.isRequest()) {
					send(requestHandlerMapping.getHandler((RequestHeaders) incomingMessage).handleRequest((RequestMessage) incomingMessage));
				}
				else {
					responseReceived((IncomingResponseMessage) incomingMessage);
				}
			}
		}
		catch (IOException ex) {
			// exceptions are handled by send and receive
		}
	}
	
	private void responseReceived(IncomingResponseMessage message) {
		responsesLock.lock();
		try {
			responses.put(message.getInResponseTo(), message);
			responseReceived.signalAll();
		}
		finally {
			responsesLock.unlock();
		}
	}
	
	public ResponseMessage awaitResponse(RequestMessage message) throws InterruptedException {
		if(!running) {
			throw new IllegalStateException("Not running");
		}
		
		ResponseMessage result;
		responsesLock.lock();
		try {
			result = responses.remove(message.getMessageId());
			while (result == null && running) {
				responseReceived.await();
				result = responses.remove(message.getMessageId());
			}
		}
		finally {
			responsesLock.unlock();
		}

		return result;
	}
	
	private IncomingHeaders recieve() throws IOException {
		synchronized (in) {
			try {
				return AbstractMessage.read(this, in);
			}
			catch (IOException ex) {
				throw handle(ex);
			}
		}
	}
	
	public void send(OutgoingMessage message) throws IOException {
		outLock.lock();
		try {
			message.write(out);
		}

		catch (IOException ex) {
			throw handle(ex);
		}
		finally {
			outLock.unlock();
		}
	}
	
	/** Return an output stream for the content of the message.
	 * The headers are written before the first content is written.
	 * Content should be written and the stream closed as soon as possible as
	 * other threads are unable to send messages from the time the first content
	 * is written to the time the stream is closed. If the number of bytes
	 * written is different from the declared Content-Length, the client's
	 * connection will fail or lose messages.
	 */
	public OutputStream getOutputStream(OutgoingHeaders message) {
		return new ConnectionOutputStream(out, message);
	}
	
	private <T extends Throwable> T handle(T t) {
		try {
			close();
		}
		catch (IOException ex) {
			// TODO log
		}
		
		return t;
	}
	
	/** Return a message id for a message.
	 * The message id is guaranteed to be unique for the duration of the connection.
	 */
	public String generateMessageId() {
		return messageIdSource.generateMessageId();
	}
	
	public void close() throws IOException {
		running = false;
		responsesLock.lock();
		responseReceived.signalAll();
		responsesLock.unlock();
		
		// TODO
		try {
			socket.close();
		}
		finally {
			manager.connectionClosed(this);
		}
	}
	
	private class ConnectionOutputStream extends FilterOutputStream {
		private OutgoingHeaders headers;
		private boolean closed = false;
		
		private ConnectionOutputStream(OutputStream out, OutgoingHeaders headers) {
			super(out);
		}

		@Override
		public void close() throws IOException {
			if (outLock.isHeldByCurrentThread()) {
				// TODO shouldn't always be LF
				try {
					out.write('\n');
					out.flush();
				}
				catch (IOException ex) {
					throw handle(ex);
				}
				finally {
					closed = true;
					outLock.unlock();
				}
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			try {
				onWrite();
				super.write(b, off, len);
			}
			catch (IOException ex) {
				throw handle(ex);
			}
		}

		@Override
		public void write(byte[] b) throws IOException {
			try {
				onWrite();
				super.write(b);
			}
			catch (IOException ex) {
				throw handle(ex);
			}
		}

		@Override
		public void write(int b) throws IOException {
			try {
				onWrite();
				super.write(b);
			}
			catch (IOException ex) {
				throw handle(ex);
			}
		}
		
		private void onWrite() throws IOException {
			if (closed) {
				throw new IllegalStateException("Stream closed");
			}
			if (!outLock.isHeldByCurrentThread()) {
				outLock.lock();
				if (headers.getContentLength() <= 0) {
					throw new IllegalStateException("Content length is 0");
				}
				
				try {
					headers.writeHeaders(out);
				}
				catch (IOException ex) {
					throw handle(ex);
				}
			}
		}
	}
}
