/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import java.io.IOException;
import java.io.OutputStream;

/** A set of headers that can be written to an output stream.
 * @author rberdeen
 *
 */
public interface OutgoingHeaders extends Headers {
	public void setContentType(String contentType);
	
	public void setHeader(String header, String value);
	
	/** Write the headers, including the request/response line, to the output stream.
	 * The headers include the blank line that separates headers from content.
	 */
	public void writeHeaders(OutputStream out) throws IOException;
}
