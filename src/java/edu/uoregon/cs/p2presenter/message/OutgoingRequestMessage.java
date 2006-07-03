/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingRequestMessage extends RequestMessageImpl implements OutgoingMessage {

	public OutgoingRequestMessage(MessageIdSource messageIdSource) {
		setHeader(SpecialHeader.Message_Id, messageIdSource.generateMessageId());
	}
	
	public OutgoingRequestMessage(MessageIdSource messageIdSource, byte[] content) {
		this(messageIdSource);
		setContent(content);
	}
	
	public OutgoingRequestMessage(MessageIdSource messageIdSource, CharSequence content) {
		this(messageIdSource);
		setContent(content);
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
