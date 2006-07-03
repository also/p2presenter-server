/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public interface Headers {
	public boolean hasContent();
	
	public int getContentLength();
	public String getHeader(String name);
	
	public boolean isRequest();
}
