/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.uoregon.cs.p2presenter.message.DefaultMessageIdSource;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.MessageIdSource;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;

public abstract class AbstractConnection implements Connection {
	private String connectionId;
	
	private long lastMessageRecievedTime = -1;
	private long lastMessageSentTime = -1;

	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	private ArrayList<ConnectionListener> connectionListeners = new ArrayList<ConnectionListener>();
	
	private MessageIdSource messageIdSource = new DefaultMessageIdSource();
	
	private static final ResponseHandler<IncomingResponseMessage> DEFAULT_RESPONSE_HANDLER = new ResponseHandler<IncomingResponseMessage>() {
		public IncomingResponseMessage handleResponse(IncomingResponseMessage response) {
			return response;
		}
	};
	
	public AbstractConnection() {}
	
	public AbstractConnection(String connectionId) {
		this.connectionId = connectionId;
	}
	
	public String getConnectionId() {
		return connectionId;
	}
	
	protected void onSend() {
		lastMessageSentTime = System.currentTimeMillis();
	}
	
	protected void onRecieve() {
		lastMessageRecievedTime = System.currentTimeMillis();
	}
	
	public long getLastMessageRecievedTime() {
		return lastMessageRecievedTime;
	}
	
	public long getLastMessageSentTime() {
		return lastMessageSentTime;
	}
	
	public void addConnectionListener(ConnectionListener connectionListener) {
		this.connectionListeners.add(connectionListener);
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
	
	/** Return a message id for a message.
	 * The message id is guaranteed to be unique for the duration of the connection.
	 */
	public String generateMessageId() {
		return messageIdSource.generateMessageId();
	}
	
	/** Sends a request whose response will be handled by the caller.
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public Future<IncomingResponseMessage> sendRequest(OutgoingRequestMessage request) throws IOException {
		return sendRequest(request, DEFAULT_RESPONSE_HANDLER);
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
	
	public void onClose() {
		for (ConnectionListener connectionListener : connectionListeners) {
			connectionListener.connectionClosed(this);
		}
	}

}
