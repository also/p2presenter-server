/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public interface Message extends Headers {
	public byte[] getContent();
	
	/** Return the content of the message as a String.
	 * Content is decoded using UTF-8.
	 */
	public String getContentAsString();
}
