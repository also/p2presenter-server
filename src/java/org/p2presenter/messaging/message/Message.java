/* $Id$ */

package org.p2presenter.messaging.message;

public interface Message extends Headers {
	/** Return the content of the message.
	 */
	public byte[] getContent();
	
	/** Return the content of the message as a String.
	 * Content is decoded using UTF-8.
	 */
	public String getContentAsString();
}
