package edu.uoregon.cs.p2presenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Message {
	protected final Map<String, String> headers = new HashMap<String, String>();
	
	protected byte[] content;
	
	protected Message() {}
	
	public final boolean hasContent() {
		return content != null;
	}
	
	public final byte[] getContent() {
		return content;
	}
	
	public final String getContentAsString() {
		try {
			return new String(content, "UTF-8");
		}
		catch (UnsupportedEncodingException ex) {
			throw new Error(ex);
		}
	}
	
	public final int getContentLength() {
		return content.length;
	}
	
	public final String getHeader(String name) {
		return headers.get(name);
	}
	
	public static Message read(PushbackInputStream in) throws IOException {
		Message result = new Message();
		String line;
		while(!"".equals(line = readLine(in))) {
			int colonPosition = line.indexOf(':');
			
			// TODO we don't allow empty headers
			if(colonPosition + 3 > line.length() || line.charAt(colonPosition + 1) != ' ') {
				// FIXME
				throw new RuntimeException("Invalid header: " + line);
			}
			result.headers.put(line.substring(0, colonPosition), line.substring(colonPosition + 2));
		}
		
		String contentLengthHeader = result.headers.get("Content-Length");
		String contentHeader = result.headers.get("Content");
		
		if(contentLengthHeader != null) {
			try {
				result.content = new byte[Integer.parseInt(contentLengthHeader)];
			}
			catch (Exception ex) {
				// FIXME
				throw new RuntimeException("Invalid Content-Length: " + contentLengthHeader);
			}
			
			if(in.read(result.content) != result.content.length) {
				// FIXME pretty sure this is wrong
				throw new IOException("Input unavailable");
			}
			
			// Content must be followed by a newline
			if(!"".equals(readLine(in))) {
				// FIXME
				throw new RuntimeException("Invalid input");
			}
		}
		else if(contentHeader != null) {
			result.content = contentHeader.getBytes("UTF-8");
		}
		
		return result;
	}
	
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
}
