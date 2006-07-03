/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public interface Message {
	public boolean hasContent();
	
	public int getContentLength();
	
	public byte[] getContent();
	public String getContentAsString();
	
	public String getHeader(String name);
	
	public boolean isRequest();
}
