/* $Id$ */

package org.p2presenter.messaging.message;

import org.p2presenter.messaging.IdGenerator;

public class OutgoingRequestMessage extends AbstractRequestMessage implements OutgoingMessage {
	private static final String DEFAULT_REQUEST_TYPE = "GET";
	
	public OutgoingRequestMessage(IdGenerator messageIdSource) {
		this(messageIdSource, "*");
	}
	
	public OutgoingRequestMessage(IdGenerator idSource, String url) {
		this(idSource, DEFAULT_REQUEST_TYPE, url);
	}
	
	public OutgoingRequestMessage(IdGenerator idSource, String requestType, String url) {
		super(requestType, url);
		setHeader(MESSAGE_ID, idSource.generateId());
	}
	
	public OutgoingRequestMessage(IdGenerator idSource, AbstractRequestMessage that) {
		super(that);
		setHeader(MESSAGE_ID, idSource.generateId());
	}
	
	@Override
	public final void setRequestType(String requestType) {
		super.setRequestType(requestType);
	}
	
	@Override
	public final void setUri(String uri) {
		super.setUri(uri);
	}
	
	@Override
	public final void setHeader(String name, String value) {
		super.setHeader(name, value);
	}
	
	@Override
	public void setContentType(String contentType) {
		super.setContentType(contentType);
	}

	@Override
	public final void setContent(byte[] content) {
		super.setContent(content);
	}
	
	@Override
	public void setContent(byte[] content, String contentType) {
		super.setContent(content, contentType);
	}

	@Override
	public final void setContent(CharSequence content) {
		super.setContent(content);
	}
}
