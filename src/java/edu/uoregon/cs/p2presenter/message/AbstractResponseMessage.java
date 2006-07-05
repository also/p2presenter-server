/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public abstract class AbstractResponseMessage extends AbstractMessage implements ResponseMessage {
	private int status;
	private String reason;
	
	protected AbstractResponseMessage(int status) {
		this.status = status;
	}
	
	protected AbstractResponseMessage(int status, String reason) {
		this(status);
		this.reason = reason;
	}
	
	public final int getStatus() {
		return status;
	}
	
	protected void setStatus(int status) {
		this.status = status;
	}
	
	public final String getReason() {
		return reason;
	}
	
	protected void setReason(String reason) {
		this.reason = reason;
	}
	
	public final boolean isRequest() {
		return false;
	}
	
	public final String getInResponseTo() {
		return getHeader(SpecialHeader.In_Response_To);
	}

	@Override
	protected final String getStartLine() {
		// TODO reason
		return Connection.PROTOCOL_VERSION + ' ' + getStatus() + " None";
	}
}
