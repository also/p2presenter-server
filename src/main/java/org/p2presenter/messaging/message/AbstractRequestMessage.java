/* $Id$ */

package org.p2presenter.messaging.message;

import org.p2presenter.messaging.LocalConnection;

/** Superclass for request messages.
 * @author rberdeen
 *
 */
public abstract class AbstractRequestMessage extends AbstractMessage implements RequestMessage {
	private String requestType;
	private String uri;
	
	public AbstractRequestMessage(String requestType, String uri) {
		this.requestType = requestType;
		this.uri = uri;
	}
	
	protected AbstractRequestMessage(AbstractRequestMessage that) {
		super(that);
		this.requestType = that.requestType;
		this.uri = that.uri;
	}
	
	public final boolean isRequest() {
		return true;
	}
	
	public final String getRequestType() {
		return requestType;
	}
	
	protected void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public final String getUri() {
		return uri;
	}
	
	protected void setUri(String uri) {
		this.uri = uri;
	}
	
	public final String getMessageId() {
		return getHeader(MESSAGE_ID);
	}
	
	@Override
	protected final String getStartLine() {
		return requestType + ' ' + uri + ' ' + LocalConnection.PROTOCOL_VERSION;
	}
}
