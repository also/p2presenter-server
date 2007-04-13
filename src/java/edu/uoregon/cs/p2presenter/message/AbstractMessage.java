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

import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.message.RequestHeaders.RequestType;

/** Superclass for message classes.
 * @author rberdeen
 *
 */
public abstract class AbstractMessage implements Message {
	private final HashMap<String, String> headers;
	
	private byte[] content;
	private CharSequence contentCharSequence;
	
	protected AbstractMessage() {
		headers = new HashMap<String, String>();
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractMessage(AbstractMessage that) {
		this.headers = (HashMap<String, String>) that.headers.clone();
		this.headers.remove(CONTENT_LENGTH);
		if (that.content != null) {
			this.content = (byte[]) that.content.clone();
		}
		else if (that.contentCharSequence != null) {
			this.contentCharSequence = that.contentCharSequence.toString();
		}
	}
	
	public final boolean hasContent() {
		return content != null || contentCharSequence != null;
	}
	
	public final byte[] getContent() {
		if (contentCharSequence != null) {
			try {
				return contentCharSequence.toString().getBytes("UTF-8");
			}
			catch (UnsupportedEncodingException ex) {
				// utf-8 should always be supported
				throw new Error(ex);
			}
		}
		else {
			return content;
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
				// utf-8 should always be supported
				throw new Error(ex);
			}
		}
		else {
			return null;
		}
	}
	
	public final int getContentLength() {
		byte[] content = getContent();
		if (content != null) {
			return content.length;
		}
		else {
			return -1;
		}
	}
	
	public final String getContentType() {
		return getHeader(CONTENT_TYPE);
	}
	
	/** Sets the Content-Type header of the message.
	 * @param contentType
	 */
	protected void setContentType(String contentType) {
		setHeader(CONTENT_TYPE, contentType);
	}
	
	public final String getHeader(String name) {
		return headers.get(name);
	}
	
	protected void setHeader(String name, String value) {
		if(containsLineEnd(name)) {
			throw new IllegalArgumentException("End of line in header name: '" + name + "'");
		}
		if(value != null && containsLineEnd(value)) {
			throw new IllegalArgumentException("End of line in header value: '" + value + "'");
		}
		setHeaderUnchecked(name, value);
	}
	
	/** Set a header without checking for line breaks.
	 */
	protected void setHeaderUnchecked(String name, String value) {
		headers.put(name, value);
	}
	
	/** Sets the character content of the message.
	 * If the content type of the message has not been set, the content type is set to <code>text/plain</code>.
	 * @param contentCharSequence
	 */
	protected void setContent(CharSequence contentCharSequence) {
		content = null;
		if (contentCharSequence == null || contentCharSequence.length() == 0) {
			this.contentCharSequence = null;
		}
		else {
			this.contentCharSequence = contentCharSequence;
		}
		if(getHeader(CONTENT_TYPE) == null) {
			setHeader(CONTENT_TYPE, "text/plain");
		}
	}
	
	/** Sest the character content of the message, along with the content type.
	 * @param content
	 * @param contentType
	 */
	protected void setContent(CharSequence content, String contentType) {
		setContent(content);
		setContentType(contentType);
	}
	
	/** Sets the byte content of the message.
	 * @param content
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
	
	protected void setContent(byte[] content, String contentType) {
		setContent(content);
		setContentType(contentType);
	}
	
	/** Return true if the String contains \r or \n. */
	protected final boolean containsLineEnd(String string) {
		for(char character : string.toCharArray()) {
			if (character == '\r' || character == '\n') {
				return true;
			}
		}
		return false;
	}
	
	/** Reads a messge. 
	 * @param connection
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static final IncomingMessage read(LocalConnection connection, PushbackInputStream in) throws IOException {
		AbstractMessage result;
		String line;
		
		/* read the request or response line */
		try {
			line = readLine(in);
		}
		catch (EOFException ex) {
			// TODO the EOF could have been encountered after the beginning of the line, in which case it really is an error
			return null;
		}
		
		/* responses start with the protocol string */
		if (line.startsWith(LocalConnection.PROTOCOL)) {
			int indexOfStatus = line.indexOf(' ', LocalConnection.PROTOCOL.length() + 1) + 1;
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
			if (line.charAt(colonPosition + 1) != ' ') {
				throw new MessageParsingException("Invalid header: " + line);
			}
			
			String name = line.substring(0, colonPosition);
			
			/* more than one header of the same name are combined into a comma separated list */
			String value = result.getHeader(name);
			if (value != null) {
				value += ',' + line.substring(colonPosition + 2);
			}
			else {
				value = line.substring(colonPosition + 2);
			}
			
			result.setHeaderUnchecked(name, value);
		}
		
		String contentLengthHeader = result.getHeader(CONTENT_LENGTH);
		
		if (contentLengthHeader != null) {
			byte[] content;
			try {
				content = new byte[Integer.parseInt(contentLengthHeader)];
			}
			catch (NumberFormatException ex) {
				throw new MessageParsingException("Invalid Content-Length: " + contentLengthHeader);
			}
			
			if (in.read(content) != content.length) {
				// FIXME pretty sure this is wrong
				throw new MessageParsingException("Content unavailable");
			}
			
			result.setContent(content);
		}
		
		// Message must be followed by a newline
		if (!"".equals(readLine(in))) {
			throw new MessageParsingException("Incorrect Content-Length");
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
	
	public final void writeHeaders(OutputStream out) throws IOException {
		writeHeaders(new PrintWriter(new OutputStreamWriter(out)), true);
	}
	
	private void writeHeaders(PrintWriter writer, boolean flushHeaders) throws IOException {
		
		/* send the request or response line */
		writer.println(getStartLine());
		
		/* send the headers */
		for(Map.Entry<String, String> header : headers.entrySet()) {
			if (header.getValue() != null) {
				writer.println(header.getKey() + ": " + header.getValue());
			}
		}
		
		if (flushHeaders) {
			writer.println();
			writer.flush();
		}
		
		if (writer.checkError()) {
			throw new IOException("Couldn't write headers");
		}
	}
	
	/** Writes the message to the output stream.
	 * @param out
	 * @throws IOException
	 */
	public final void write(OutputStream out) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
		
		writeHeaders(writer, false);
		
		/* send the content, if any */
		byte[] content = getContent();
		if(content != null) {
			writer.println("Content-Length: " + content.length);
			writer.println();
			writer.flush();
			out.write(content);
		}
		else {
			writer.println();
		}
		
		/* messages are separated by a blank line */
		writer.println();
		
		/* don't leave anything in the buffer */
		writer.flush();
		if (writer.checkError()) {
			throw new IOException("Couldn't write message body");
		}
	}
	
	/** Returns the first line of a message,
	 * which indicates whether the message is a request or a response.
	 */
	protected abstract String getStartLine();
}
