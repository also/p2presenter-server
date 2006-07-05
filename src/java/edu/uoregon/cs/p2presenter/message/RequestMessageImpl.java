/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.Connection;

public class RequestMessageImpl extends AbstractMessage implements RequestMessage {
	private RequestType requestType;
	private String url;
	
	public RequestMessageImpl(RequestType requestType, String url) {
		this.requestType = requestType;
		this.url = url;
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
	
	public final String getUrl() {
		return url;
	}
	
	protected void setUrl(String url) {
		this.url = url;
	}
	
	public final String getMessageId() {
		return getHeader(SpecialHeader.Message_Id);
	}
	
	@Override
	protected final String getStartLine() {
		return requestType.name() + ' ' + url + ' ' + Connection.PROTOCOL_VERSION;
	}
}
