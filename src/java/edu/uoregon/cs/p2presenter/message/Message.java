/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public interface Message extends Headers {
	public byte[] getContent();
	public String getContentAsString();
}
