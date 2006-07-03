package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class OutgoingMessage extends MessageImpl {
	
	public OutgoingMessage(String messageId) {
		setHeader(SpecialHeader.Message_Id, messageId);
	}
	
	public OutgoingMessage(MessageImpl inResponseToMessage) {
		setHeader(SpecialHeader.In_Response_To, inResponseToMessage.getMessageId());
	}
	
	public OutgoingMessage(String messageId, CharSequence content) {
		this(messageId);
		setContent(content);
	}
	
	public OutgoingMessage(MessageImpl inResponseToMessage, CharSequence content) {
		this(inResponseToMessage);
		setContent(content);
	}
	
	public void setHeader(String name, String value) {
		if(SpecialHeader.isSpecialHeader(name)) {
			throw new IllegalArgumentException(name + " header may not be set manually");
		}
		headers.put(name, value);
	}
	
	private void setHeader(SpecialHeader header, String value) {
		headers.put(header.getName(), value);
	}
	
	public void setContent(CharSequence content) {
		try {
			this.content = content.toString().getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException ex) {
			throw new Error(ex);
		}
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public void setStatus(int status) {
		setHeader(SpecialHeader.Status, String.valueOf(status));
	}
	
	@Override
	public String getMessageId() {
		return getHeader(SpecialHeader.Message_Id);
	}
	
	@Override
	public String getInResponseTo() {
		return getHeader(SpecialHeader.In_Response_To);
	}
	
	void write(OutputStream out) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
		String messageId = getHeader(SpecialHeader.Message_Id);
		if(messageId != null) {
			writer.println("Message-Id: " + messageId);
		}
		else {
			writer.println("In-Response-To: " + getHeader(SpecialHeader.In_Response_To));
		}
		
		for(Map.Entry<String, String> header : headers.entrySet()) {
			writer.println(header.getKey() + ": " + header.getValue());
		}
		
		if(content != null) {
			writer.println("Content-Length: " + content.length);
			writer.println();
			writer.flush();
			out.write(content);
		}
		
		writer.println();
		writer.flush();
	}
}
