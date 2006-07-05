/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingRequestMessage extends RequestMessageImpl implements OutgoingMessage {

	public OutgoingRequestMessage(MessageIdSource messageIdSource) {
		this(messageIdSource, RequestType.GET, "*");
	}
	
	public OutgoingRequestMessage(MessageIdSource messageIdSource, RequestType requestType, String url) {
		super(requestType, url);
		setHeader(SpecialHeader.Message_Id, messageIdSource.generateMessageId());
	}
	
	@Override
	public void setRequestType(RequestType requestType) {
		super.setRequestType(requestType);
	}
	
	@Override
	public final void setHeader(String name, String value) {
		super.setHeader(name, value);
	}

	@Override
	public final void setContent(byte[] content) {
		super.setContent(content);
	}

	@Override
	public final void setContent(CharSequence content) {
		super.setContent(content);
	}
}
