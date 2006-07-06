/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

/** Superclass for message classes.
 * @author rberdeen
 *
 */
public abstract class AbstractMessage implements Message {
	private final Map<String, String> headers = new HashMap<String, String>();
	
	public static final byte[] EMPTY_CONTENT = new byte[0];
	public static final CharSequence EMPTY_CHAR_SEQUENCE_CONTENT = "";
	
	private byte[] content;
	private CharSequence contentCharSequence;
	
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
		return content != null || contentCharSequence != null;
	}
	
	public final byte[] getContent() {
		if (content != null) {
			return content;
		}
		else if (contentCharSequence != null) {
			try {
				return contentCharSequence.toString().getBytes("UTF-8");
			}
			catch (UnsupportedEncodingException ex) {
				throw new Error(ex);
			}
		}
		else {
			return EMPTY_CONTENT;
		}
	}
	
	public final String getContentAsString() {
		if (contentCharSequence != null) {
			return contentCharSequence.toString();
		}
		else if (content != null) {
			try {
				return new String(content, "UTF-8");
			}
			catch (UnsupportedEncodingException ex) {
				throw new Error(ex);
			}
		}
		else {
			return (String) EMPTY_CHAR_SEQUENCE_CONTENT;
		}
	}
	
	public final int getContentLength() {
		return getContent().length;
	}
	
	public final String getHeader(String name) {
		return headers.get(name);
	}
	
	protected final String getHeader(SpecialHeader header) {
		return getHeader(header.getName());
	}
	
	protected final void setHeader(SpecialHeader header, String value) {
		headers.put(header.getName(), value);
	}
	
	protected void setHeader(String name, String value) {
		if(SpecialHeader.isSpecialHeader(name)) {
			throw new IllegalArgumentException(name + " header may not be set manually");
		}
		if(containsLineEnd(name)) {
			throw new IllegalArgumentException("End of line in header name");
		}
		if(containsLineEnd(value)) {
			throw new IllegalArgumentException("End of line in header value");
		}
		setHeaderUnchecked(name, value);
	}
	
	/** Set a header without checking for line breaks or special headers.
	 */
	private void setHeaderUnchecked(String name, String value) {
		headers.put(name, value);
	}
	
	/** Set the content as a CharSequence.
	 * If the <code>Content-Type</code> has not been set, it is set to <code>text/plain</code>.
	 */
	protected void setContent(CharSequence contentCharSequence) {
		content = null;
		if (contentCharSequence == null || contentCharSequence.length() == 0) {
			this.contentCharSequence = null;
		}
		else {
			this.contentCharSequence = contentCharSequence;
		}
		if(getHeader(SpecialHeader.Content_Type) == null) {
			setHeader(SpecialHeader.Content_Type, "text/plain");
		}
	}
	
	/** Set the content as a byte array.
	 */
	protected void setContent(byte[] content) {
		contentCharSequence = null;
		if (content == null || content.length == 0) {
			this.content = null;
		}
		else {
			this.content = content;
		}
	}
	
	protected final boolean containsLineEnd(String string) {
		for(char character : string.toCharArray()) {
			if (character == '\r' || character == '\n') {
				return false;
			}
		}
		return true;
	}
	
	public static final IncomingMessage read(Connection connection, PushbackInputStream in) throws IOException {
		AbstractMessage result;
		
		/* read the request or response line */
		String line = readLine(in);
		
		/* responses start with the protocol string */
		if (line.startsWith(Connection.PROTOCOL)) {
			int indexOfStatus = line.indexOf(' ', Connection.PROTOCOL.length() + 1) + 1;
			int indexOfReasonPhrase = line.indexOf(' ', indexOfStatus + 1) + 1;
			result = new IncomingResponseMessage(connection, Integer.parseInt(line.substring(indexOfStatus, indexOfReasonPhrase - 1)), line.substring(indexOfReasonPhrase));
		}
		/* otherwise the message is a request */
		else {
			int indexOfUrl  = line.indexOf(' ') + 1;
			result = new IncomingRequestMessage(connection, RequestType.valueOf(line.substring(0, indexOfUrl - 1)), line.substring(indexOfUrl, line.indexOf(' ', indexOfUrl + 1)));
		}
		
		/* read the headers. the headers are separated from what follows by a blank line */
		while (!"".equals(line = readLine(in))) {
			int colonPosition = line.indexOf(':');
			
			// TODO we don't allow empty headers
			if (colonPosition + 3 > line.length() || line.charAt(colonPosition + 1) != ' ') {
				throw new MessageParsingException("Invalid header: " + line);
			}
			
			String name = line.substring(0, colonPosition);
			
			/* more than one header of the same name are combined into a comma separated list */
			String value = result.headers.get(name);
			if (value != null) {
				value += ',' + line.substring(colonPosition + 2);
			}
			else {
				value = line.substring(colonPosition + 2);
			}
			
			result.setHeaderUnchecked(name, value);
		}
		
		String contentLengthHeader = result.getHeader(SpecialHeader.Content_Length);
		
		if (contentLengthHeader != null) {
			try {
				result.setContent(new byte[Integer.parseInt(contentLengthHeader)]);
			}
			catch (Exception ex) {
				throw new MessageParsingException("Invalid Content-Length: " + contentLengthHeader);
			}
			
			if (in.read(result.content) != result.content.length) {
				// FIXME pretty sure this is wrong
				throw new MessageParsingException("Content unavailable");
			}
			
			// Content must be followed by a newline
			if (!"".equals(readLine(in))) {
				throw new MessageParsingException("Incorrect Content-Length");
			}
		}
		
		return (IncomingMessage) result;
	}
	
	// TODO will this work with multi-byte characters?
	private static String readLine(PushbackInputStream in) throws IOException {
		int b;
		boolean sawCR = false;
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		
		for (;;) {
			b = in.read();
			if(b == -1) {
				throw new EOFException("Expecting message line");
			}
			else if(b == '\n') {
				break;
			}
			else if (sawCR) {
				in.unread(b);
				break;
			}
			else if(b =='\r') {
				sawCR = true;
			}
			else {
				bytes.write(b);
			}
		}
		
		return bytes.toString("UTF-8");
	}
	
	public final void write(OutputStream out) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
		
		/* send the request or response line */
		writer.println(getStartLine());
		
		/* send the headers */
		for(Map.Entry<String, String> header : headers.entrySet()) {
			writer.println(header.getKey() + ": " + header.getValue());
		}
		
		/* send the content, if any */
		byte[] content = getContent();
		if(content != EMPTY_CONTENT) {
			writer.println("Content-Length: " + content.length);
			writer.println();
			writer.flush();
			out.write(content);
		}
		
		/* messages are separated by a blank line */
		writer.println();
		
		/* don't leave anything in the buffer */
		writer.flush();
	}
	
	protected abstract String getStartLine();
}
