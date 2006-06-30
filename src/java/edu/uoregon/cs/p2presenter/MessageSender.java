package edu.uoregon.cs.p2presenter;

import java.io.IOException;

public class MessageSender {
	private Connection connection;
	
	private OutgoingMessage currentMessage;
	
	public MessageSender(Connection connection) {
		this.connection = connection;
		reInit();
	}
	
	public void setHeader(String name, String value) {
		currentMessage.setHeader(name, value);
	}
	
	public void setContent(byte[] content) {
		currentMessage.setContent(content);
	}
	
	public void setContent(CharSequence content) {
		currentMessage.setContent(content);
	}
	
	public OutgoingMessage send() throws IOException {
		OutgoingMessage result = currentMessage;
		connection.write(currentMessage);
		reInit();
		
		return result;
	}
	
	public void cancel() {
		reInit();
	}
	
	private void reInit() {
		currentMessage = new OutgoingMessage(connection.generateMessageId());
	}
}
