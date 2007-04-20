/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.IdGenerator;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

public interface Connection extends IdGenerator {
	public String getConnectionId();

	public void addConnectionListener(ConnectionListener connectionListener);

	public void setAttribute(String key, Object value);

	public Object getAttribute(String key);

	/** Returns the specified attribute, initializing it with the result of the {@link Callable} if it does not exist.
	 * This method is thread safe.
	 */
	public Object getAttribute(String key, Callable<?> defaultValueCallable);

	public long getLastMessageRecievedTime();

	public long getLastMessageSentTime();

	/** Sends a request whose response will be handled by the caller.
	 * @param request
	 * @return a {@link Future} providing access to the response message
	 * @throws IOException
	 */
	public Future<IncomingResponseMessage> sendRequest(
			OutgoingRequestMessage request) throws IOException;

	/** Sends a request whose response will be handled by the specified handler.
	 * @param <V> the result type of the<code>handleResponse</code> method
	 * @param request the request message to send
	 * @param responseHandler the response message handler
	 * @return a {@link Future} allowing access to the result of the response handler
	 * @throws IOException if an exception occurs while sending the message
	 */
	public <V> Future<V> sendRequest(OutgoingRequestMessage request,
			ResponseHandler<V> responseHandler) throws IOException;

	public IncomingResponseMessage sendRequestAndAwaitResponse(
			OutgoingRequestMessage request) throws IOException,
			InterruptedException;

	/** Sends a response message. The connection will be closed if any exceptions occur.
	 * @throws IOException if an exception occurrs while sending the message
	 */
	public void sendResponse(OutgoingResponseMessage response)
			throws IOException;

}