/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public interface ResponseMessage extends Message {
	public int getStatus();
	public String getInResponseTo();
}
