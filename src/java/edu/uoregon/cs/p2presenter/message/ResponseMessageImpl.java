/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public class ResponseMessageImpl extends AbstractMessage implements ResponseMessage {
	private int status;
	
	protected ResponseMessageImpl() {
		status = 200;
	}
	
	protected ResponseMessageImpl(int status) {
		this.status = status;
	}
	
	public final boolean isRequest() {
		return false;
	}
	
	public final String getInResponseTo() {
		return getHeader(SpecialHeader.In_Response_To);
	}
	
	public final int getStatus() {
		return status;
	}
	
	protected void setStatus(int status) {
		this.status = status;
	}

	@Override
	protected final String getStartLine() {
		// TODO reason
		return Connection.PROTOCOL_VERSION + ' ' + getStatus() + " None";
	}
}
