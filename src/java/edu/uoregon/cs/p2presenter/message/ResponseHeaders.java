/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public interface ResponseHeaders {
	public int getStatus();
	public String getReason();
	public String getInResponseTo();
}
