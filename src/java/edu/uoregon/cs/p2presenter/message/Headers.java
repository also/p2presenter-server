/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

/** Headers that are available for all messages.
 * Headers may be available before the message body has been read.
 * @author rberdeen
 *
 */
public interface Headers {
	
	/** Return the value of the named header.
	 */
	public String getHeader(String name);
	
	/** Return true if the message has content.
	 * A message has content if and only if the Content-Length header is set.
	 */
	public boolean hasContent();
	
	/** Return the length of the content in bytes.
	 * If the message has no content, returns -1.
	 */
	public int getContentLength();
	
	/** Return true if the message is a request.
	 */
	public boolean isRequest();
	
	
}
