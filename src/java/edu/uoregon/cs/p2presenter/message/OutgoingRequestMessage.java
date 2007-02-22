/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingRequestMessage extends AbstractRequestMessage implements OutgoingMessage {

	public OutgoingRequestMessage(IdGenerator messageIdSource) {
		this(messageIdSource, RequestType.GET, "*");
	}
	
	public OutgoingRequestMessage(IdGenerator idSource, RequestType requestType, String url) {
		super(requestType, url);
		setHeader(SpecialHeader.Message_Id, idSource.generateId());
	}
	
	public OutgoingRequestMessage(IdGenerator idSource, AbstractRequestMessage that) {
		super(that);
		setHeader(SpecialHeader.Message_Id, idSource.generateId());
	}
	
	@Override
	public final void setRequestType(RequestType requestType) {
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
