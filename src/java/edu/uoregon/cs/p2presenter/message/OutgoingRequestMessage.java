/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingRequestMessage extends AbstractRequestMessage implements OutgoingMessage {

	public OutgoingRequestMessage(MessageIdSource messageIdSource) {
		this(messageIdSource, RequestType.GET, "*");
	}
	
	public OutgoingRequestMessage(MessageIdSource messageIdSource, RequestType requestType, String url) {
		super(requestType, url);
		setHeader(SpecialHeader.Message_Id, messageIdSource.generateMessageId());
	}
	
	public OutgoingRequestMessage(MessageIdSource messageIdSource, IncomingRequestMessage that) {
		super(that);
		setHeader(SpecialHeader.Message_Id, messageIdSource.generateMessageId());
		setHeader(SpecialHeader.Proxied_Connection_Id, that.getConnection().getConnectionId().toString());
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
