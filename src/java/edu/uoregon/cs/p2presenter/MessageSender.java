package edu.uoregon.cs.p2presenter;

import java.io.IOException;

public class MessageSender {
	private Connection connection;
	private OutgoingMessageStream out;
	
	private OutgoingMessage currentMessage;
	
	public MessageSender(Connection connection) {
		this.connection = connection;
		out = connection.getOut();
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
	
	public void send() throws IOException {
		out.write(currentMessage);
		reInit();
	}
	
	public void cancel() {
		reInit();
	}
	
	private void reInit() {
		currentMessage = new OutgoingMessage(connection.generateMessageId());
	}
}
