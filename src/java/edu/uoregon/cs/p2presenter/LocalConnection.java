/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

import edu.uoregon.cs.p2presenter.message.AbstractMessage;
import edu.uoregon.cs.p2presenter.message.DefaultMessageIdSource;
import edu.uoregon.cs.p2presenter.message.IncomingMessage;
import edu.uoregon.cs.p2presenter.message.IncomingRequestHeaders;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.MessageIdSource;
import edu.uoregon.cs.p2presenter.message.OutgoingHeaders;
import edu.uoregon.cs.p2presenter.message.OutgoingMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

public class LocalConnection extends Thread implements MessageIdSource, Closeable {
	public static final String PROTOCOL = "P2PR";
	public static final String VERSION = "0.1";
	public static final String PROTOCOL_VERSION = PROTOCOL + '/' + VERSION;
	
	private Socket socket;
	private Integer connectionId;

	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	private ArrayList<ConnectionListener> connectionListeners = new ArrayList<ConnectionListener>();
	
	private long lastMessageRecievedTime = -1;
	private long lastMessageSentTime = -1;
	
	private BufferedOutputStream out;
	private ReentrantLock outLock = new ReentrantLock();
	private PushbackInputStream in;

	private DefaultRequestHandlerMapping requestHandlerMapping = new DefaultRequestHandlerMapping();
	
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	private HashMap<String, ResponseHandlerFutureTask<?>> responseHandlers = new HashMap<String, ResponseHandlerFutureTask<?>>();
	private static final ResponseHandler<IncomingResponseMessage> DEFAULT_RESPONSE_HANDLER = new ResponseHandler<IncomingResponseMessage>() {
		public IncomingResponseMessage handleResponse(IncomingResponseMessage response) {
			return response;
		}
	};
	
	private MessageIdSource messageIdSource = new DefaultMessageIdSource();
	
	boolean running = false;
	
	public LocalConnection(Socket socket) throws IOException {
		this.socket = socket;
		
		out = new BufferedOutputStream(socket.getOutputStream());
		in = new PushbackInputStream(socket.getInputStream());
	}
	
	public LocalConnection(Socket socket, int connectionId) throws IOException {
		this(socket);
		this.connectionId = connectionId;
	}
	
	public Integer getConnectionId() {
		return connectionId;
	}
	
	public void addConnectionListener(ConnectionListener connectionListener) {
		this.connectionListeners.add(connectionListener);
	}
	
	public DefaultRequestHandlerMapping getRequestHandlerMapping() {
		return requestHandlerMapping;
	}
	
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	/** Returns the specified attribute, initializing it with the result of the {@link Callable} if it does not exist.
	 * This method is thread safe.
	 */
	public Object getAttribute(String key, Callable<?> defaultValueCallable) {
		synchronized (attributes) {
			Object result = attributes.get(key);
			
			if (result == null) {
				try {
					result = defaultValueCallable.call();
				}
				catch (RuntimeException ex) {
					throw ex;
				}
				catch (Exception ex) {
					throw new RuntimeException(ex);
				}
				
				attributes.put(key, result);
			}
			
			return result;
		}
	}
	
	public long getLastMessageRecievedTime() {
		return lastMessageRecievedTime;
	}
	
	public long getLastMessageSentTime() {
		return lastMessageSentTime;
	}
	
	@Override
	public void run() {
		running = true;
		
		IncomingMessage message;
		
		try {
			for (;;) {
				message = AbstractMessage.read(this, in);
				lastMessageRecievedTime = System.currentTimeMillis();
				
				if (message == null) {
					close();
					// TODO more stuff
					return;
				}

				if (message.isRequest()) {
					RequestHandler requestHandler = requestHandlerMapping.getHandler((IncomingRequestHeaders) message);
					
					if (requestHandler != null) {
						RequestHandlerRunnable runnable = new RequestHandlerRunnable(requestHandler, (IncomingRequestMessage) message);
						executorService.execute(runnable);
					}
					else {
						// TODO send error response
						// TODO exception type
						throw new Exception("No handler found for uri " + ((IncomingRequestHeaders) message).getUri());
					}
				}
				else {
					IncomingResponseMessage response = (IncomingResponseMessage) message;
					ResponseHandlerFutureTask<?> handler = responseHandlers.remove(response.getInResponseTo());
					if (handler != null) {
						handler.setResponse(response);
						executorService.execute(handler);
					}
					else {
						throw new Exception("ignored response");
					}
				}
			}
		}
		catch (Throwable t) {
			// TODO shouldn't close connection on all exceptions
			handle(t);
		}
	}
	
	/** Sends a request whose response will be handled by the caller.
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public Future<IncomingResponseMessage> sendRequest(OutgoingRequestMessage request) throws IOException {
		return sendRequest(request, DEFAULT_RESPONSE_HANDLER);
	}
	
	/** Sends a request whose response will be handled by the specified handler.
	 * @param <V> the result type of the<code>handleResponse</code> method
	 * @param request the request message to send
	 * @param responseHandler the response message handler
	 * @return a {@link Future} allowing access to the result of the response handler
	 * @throws IOException if an exception occurs while sending the message
	 */
	public <V> Future<V> sendRequest(OutgoingRequestMessage request, ResponseHandler<V> responseHandler) throws IOException {
		ResponseHandlerFutureTask<V> future = new ResponseHandlerFutureTask<V>(responseHandler);
		responseHandlers.put(request.getMessageId(), future);
		doSend(request);
		return future;
	}
	
	public IncomingResponseMessage sendRequestAndAwaitResponse(OutgoingRequestMessage request) throws IOException, InterruptedException {
		try {
			return sendRequest(request).get();
		}
		catch (ExecutionException ex) {
			assert false : "Exception in defualt response handler";
			return null;
		}
	}
	
	/** Sends the message, closing the connection if an exception is thrown.
	 */
	private void doSend(OutgoingMessage message) throws IOException {
		try {
			doSendInternal(message);
		}
		catch (IOException ex) {
			throw handle(ex);
		}
	}
	
	/** Sends the message, ignoring exceptions.
	 * @param message
	 * @throws IOException
	 */
	private void doSendInternal(OutgoingMessage message) throws IOException {
		outLock.lock();
		try {
			message.write(out);
			lastMessageSentTime = System.currentTimeMillis();
		}
		finally {
			outLock.unlock();
		}
	}
	
	public void sendResponse(OutgoingResponseMessage response) throws IOException {
		doSend(response);
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
		t.printStackTrace();
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
		
		// TODO
		try {
			for (ResponseHandlerFutureTask<?> futureResponseHandler : responseHandlers.values()) {
				futureResponseHandler.cancel(false);
			}
			
			socket.close();
		}
		finally {
			for (ConnectionListener connectionListener : connectionListeners) {
				connectionListener.connectionClosed(this);
			}
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
			// TODO else IllegalStateException?
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
	
	private class RequestHandlerRunnable implements Runnable {
		private RequestHandler requestHandler;
		private IncomingRequestMessage request;
		
		public RequestHandlerRunnable(RequestHandler requestHandler, IncomingRequestMessage request) {
			this.requestHandler = requestHandler;
			this.request = request;
		}
		
		public void run() {
			try {
				OutgoingResponseMessage response =  requestHandler.handleRequest(request);
				if (response != null) {
					doSendInternal(response);
				}
			}
			catch (Throwable t) {
				// TODO shouldn't close connection on all exceptions
				handle(t);
			}
		}
	}
	
	private static class ResponseHandlerFutureTask<V> extends FutureTask<V> {
		private ResponseHandlerCallable<V> responseHandlerCallable;
		
		public ResponseHandlerFutureTask(ResponseHandler<V> responseHandler) {
			this(new ResponseHandlerCallable<V>(responseHandler));
		}
		
		private ResponseHandlerFutureTask(ResponseHandlerCallable<V> responseHandlerCallable) {
			super(responseHandlerCallable);
			this.responseHandlerCallable = responseHandlerCallable;
		}
		
		public void setResponse(IncomingResponseMessage response) {
			responseHandlerCallable.response = response;
		}
	}
	
	private static class ResponseHandlerCallable<V> implements Callable<V> {
		private ResponseHandler<V> responseHandler;
		private IncomingResponseMessage response;
		
		public ResponseHandlerCallable(ResponseHandler<V> responseHandler) {
			this.responseHandler = responseHandler;
		}
		
		public V call() throws Exception {
			// TODO handle exceptions
			return responseHandler.handleResponse(response);
		}
	}
}
