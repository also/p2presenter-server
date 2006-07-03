/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class MessageImpl implements Message {
	private final Map<String, String> headers = new HashMap<String, String>();
	
	private byte[] content;
	
	protected enum SpecialHeader {
		Content_Type,
		Content_Length,
		Content,
		Status,
		Message_Id,
		In_Response_To;
		
		private String name;
		private SpecialHeader() {
			this.name = name().replace('_', '-');
		}
		
		public String getName() {
			return name;
		}

		public static boolean isSpecialHeader(String name) {
			try {
				valueOf(name.replace('-', '_'));
				return true;
			}
			catch (IllegalArgumentException ex) {
				return false;
			}
		}
	}
	
	public final boolean hasContent() {
		return content != null;
	}
	
	public boolean isRequest() {
		return this instanceof RequestMessage;
	}
	
	public final byte[] getContent() {
		return content;
	}
	
	public final String getContentAsString() {
		if (content != null) {
			try {
				return new String(content, "UTF-8");
			}
			catch (UnsupportedEncodingException ex) {
				throw new Error(ex);
			}
		}
		else {
			return null;
		}
	}
	
	public final int getContentLength() {
		return content.length;
	}
	
	public final String getHeader(String name) {
		return headers.get(name);
	}
	
	protected final String getHeader(SpecialHeader header) {
		return getHeader(header.getName());
	}
	
	protected void setHeader(SpecialHeader header, String value) {
		headers.put(header.getName(), value);
	}
	
	protected void setHeader(String name, String value) {
		if(SpecialHeader.isSpecialHeader(name)) {
			throw new IllegalArgumentException(name + " header may not be set manually");
		}
		setHeaderUnchecked(name, value);
	}
	
	private void setHeaderUnchecked(String name, String value) {
		headers.put(name, value);
	}
	
	protected void setContent(CharSequence content) {
		try {
			this.content = content.toString().getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException ex) {
			throw new Error(ex);
		}
	}
	
	protected void setContent(byte[] content) {
		this.content = content;
	}
	
	public static final Message read(PushbackInputStream in) throws IOException {
		MessageImpl result;
		
		String line = readLine(in);

		if (line == null) {
			return null;
		}
		else if (line.startsWith("Status:")) {
			result = new ResponseMessageImpl(Integer.parseInt(line.substring(line.charAt(7) == ' ' ? 8 : 7)));
		}
		else {
			result = new RequestMessageImpl();
		}
		
		line = readLine(in);
		if (line == null) {
			return null;
		}
		do {
			int colonPosition = line.indexOf(':');
			
			// TODO we don't allow empty headers
			if (colonPosition + 3 > line.length() || line.charAt(colonPosition + 1) != ' ') {
				// FIXME
				throw new RuntimeException("Invalid header: " + line);
			}
			
			String name = line.substring(0, colonPosition);
			String value = result.headers.get(name);
			if (value != null) {
				value += ',' + line.substring(colonPosition + 2);
			}
			else {
				value = line.substring(colonPosition + 2);
			}
			
			result.setHeaderUnchecked(name, value);
			
			line = readLine(in);
		} while (!"".equals(line));
		
		String contentLengthHeader = result.getHeader(SpecialHeader.Content_Length);
		
		if (contentLengthHeader != null) {
			try {
				result.setContent(new byte[Integer.parseInt(contentLengthHeader)]);
			}
			catch (Exception ex) {
				// FIXME
				throw new RuntimeException("Invalid Content-Length: " + contentLengthHeader);
			}
			
			if (in.read(result.content) != result.content.length) {
				// FIXME pretty sure this is wrong
				throw new IOException("Input unavailable");
			}
			
			// Content must be followed by a newline
			if (!"".equals(readLine(in))) {
				// FIXME
				throw new RuntimeException("Invalid input");
			}
		}
		
		return result;
	}
	
	// TODO will this work with multi-byte characters?
	private static String readLine(PushbackInputStream in) throws IOException {
		int b;
		boolean sawCR = false;
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		
		b = in.read();
		if(b == -1) {
			return null;
		}
		do {
			if(b =='\r') {
				if(!sawCR) {
					sawCR = true;
				}
				else {
					in.unread(b);
					break;
				}
			}
			else if(b == '\n') {
				break;
			}
			else {
				bytes.write(b);
			}
		} while((b = in.read()) != -1);
		
		return bytes.toString("UTF-8");
	}
	
	public final void write(OutputStream out) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
		writeStartLine(writer);
		
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
	
	protected abstract void writeStartLine(PrintWriter writer) throws IOException;
}
