/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public class RequestMessageImpl extends AbstractMessage implements RequestMessage {
	private RequestType requestType;
	
	public RequestMessageImpl(RequestType requestType) {
		this.requestType = requestType;
	}
	
	public final boolean isRequest() {
		return true;
	}
	
	public final RequestType getRequestType() {
		return requestType;
	}
	
	protected void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	
	public final String getMessageId() {
		return getHeader(SpecialHeader.Message_Id);
	}
	
	@Override
	protected final String getStartLine() {
		// TODO url
		return requestType + " * " + Connection.PROTOCOL_VERSION;
	}
}
