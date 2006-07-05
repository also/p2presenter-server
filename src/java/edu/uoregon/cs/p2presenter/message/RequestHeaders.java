/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public interface RequestHeaders extends Headers {
	public enum RequestType {
		GET,
		EVALUATE;
	}
	
	public RequestType getRequestType();
	
	public String getUrl();
	
	public String getMessageId();
}
