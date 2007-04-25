/* $Id$ */

package org.p2presenter.messaging.message;

/** Headers that are available for all messages.
 * Headers may be available before the message body has been read.
 * @author rberdeen
 *
 */
public interface Headers {
	public static final String CONTENT_LENGTH = "Content-Length";
	public static final String CONTENT_TYPE = "Content-Type";
	
	/** Return the value of the named header.
	 */
	public String getHeader(String name);
	
	/** Return true if the message has content.
	 * A message has content if and only if the Content-Length header is set.
	 * Equivalent to getContentType() != -1.
	 */
	public boolean hasContent();
	
	/** Return the length of the content in bytes.
	 * If the message has no content, returns -1.
	 */
	public int getContentLength();
	
	public String getContentType();
	
	/** Return true if the message is a request.
	 */
	public boolean isRequest();
	
	
}
