/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.io.IOException;

import edu.uoregon.cs.p2presenter.message.OutgoingRequestMessage;
import edu.uoregon.cs.p2presenter.message.ResponseMessage;

public class MessageSender {
	private Connection connection;
	
	private OutgoingRequestMessage currentMessage;
	
	private boolean sent = false;
	
	public MessageSender(Connection connection) {
		this.connection = connection;
		reInit();
	}
	
	public void setHeader(String name, String value) {
		if(sent) {
			reInit();
		}
		currentMessage.setHeader(name, value);
	}
	
	public void setContent(byte[] content) {
		if(sent) {
			reInit();
		}
		currentMessage.setContent(content);
	}
	
	public void setContent(CharSequence content) {
		if(sent) {
			reInit();
		}
		currentMessage.setContent(content);
	}
	
	public OutgoingRequestMessage send() throws IOException {
		if(sent) {
			throw new IllegalStateException("Message already sent");
		}
		connection.write(currentMessage);
		sent = true;
		return currentMessage;
	}
	
	public ResponseMessage awaitResponse() throws InterruptedException {
		if(!sent) {
			throw new IllegalStateException("Message not sent");
		}
		return connection.awaitResponse(currentMessage);
	}
	
	public ResponseMessage sendAndAwaitResponse() throws IOException, InterruptedException {
		send();
		return awaitResponse();
	}
	
	public void cancel() {
		reInit();
	}
	
	private void reInit() {
		sent = false;
		currentMessage = new OutgoingRequestMessage(connection);
	}
}
