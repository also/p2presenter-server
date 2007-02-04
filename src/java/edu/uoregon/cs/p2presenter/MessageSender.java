/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class MessageSender {
	private LocalConnection connection;
	
	private RequestType defaultRequestType;
	private String defaultUrl;
	
	private OutgoingRequestMessage currentMessage;
	private Future<IncomingResponseMessage> currentFutureResponse; 
	
	private boolean needsReInit = true;
	
	public MessageSender(LocalConnection connection) {
		this.connection = connection;
	}
	
	public MessageSender(LocalConnection connection, RequestType defaultRequestType, String defaultUrl) {
		this(connection);
		this.defaultRequestType = defaultRequestType;
		this.defaultUrl = defaultUrl;
	}
	
	public void setRequestType(RequestType requestType) {
		reInit();
		currentMessage.setRequestType(requestType);
	}
	
	public void setUrl(String url) {
		reInit();
		currentMessage.setUri(url);
	}
	
	public void setHeader(String name, String value) {
		reInit();
		currentMessage.setHeader(name, value);
	}
	
	public void setContent(byte[] content) {
		reInit();
		currentMessage.setContent(content);
	}
	
	public void setContent(CharSequence content) {
		reInit();
		currentMessage.setContent(content);
	}
	
	public OutgoingRequestMessage send() throws IOException {
		if (needsReInit) {
			throw new IllegalStateException("Message already sent");
		}
		connection.sendRequest(currentMessage);
		needsReInit = true;
		return currentMessage;
	}
	
	public ResponseMessage awaitResponse() throws InterruptedException {
		if(!needsReInit) {
			throw new IllegalStateException("Message not sent");
		}
		try {
			return currentFutureResponse.get();
		}
		catch (ExecutionException ex) {
			// will not happen, no execution for default response handler
			return null;
		}
	}
	
	public ResponseMessage sendAndAwaitResponse() throws IOException, InterruptedException {
		return sendAndAwaitResponse();
	}
	
	public void cancel() {
		needsReInit = true;
		reInit();
	}
	
	private void reInit() {
		if (needsReInit) {
			needsReInit = false;
			if (defaultUrl != null) {
				currentMessage = new OutgoingRequestMessage(connection, defaultRequestType, defaultUrl);
			}
			else {
				currentMessage = new OutgoingRequestMessage(connection);
			}
			currentFutureResponse = null;
		}
	}
}
