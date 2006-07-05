/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;

import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

public class MessageSender {
	private Connection connection;
	
	private RequestType defaultRequestType;
	private String defaultUrl;
	
	private OutgoingRequestMessage currentMessage;
	
	private boolean needsReInit = true;
	
	public MessageSender(Connection connection) {
		this.connection = connection;
	}
	
	public MessageSender(Connection connection, RequestType defaultRequestType, String defaultUrl) {
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
		currentMessage.setUrl(url);
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
		connection.send(currentMessage);
		needsReInit = true;
		return currentMessage;
	}
	
	public ResponseMessage awaitResponse() throws InterruptedException {
		if(!needsReInit) {
			throw new IllegalStateException("Message not sent");
		}
		return connection.awaitResponse(currentMessage);
	}
	
	public ResponseMessage sendAndAwaitResponse() throws IOException, InterruptedException {
		send();
		return awaitResponse();
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
		}
	}
}
