package edu.uoregon.cs.p2presenter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class OutgoingMessage extends Message {
	private String messageId;
	public OutgoingMessage(String messageId) {
		this.messageId = messageId;
	}
	
	public OutgoingMessage(String messageId, CharSequence content) {
		this(messageId);
		setContent(content);
	}
	
	public void setHeader(String name, String value) {
		headers.put(name, value);
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
	
	protected void write(OutputStream out) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
		
		writer.println("Message-Id: " + messageId);
		
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
