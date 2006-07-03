/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingResponseMessage extends ResponseMessageImpl implements OutgoingMessage {
	public OutgoingResponseMessage(int status, RequestMessage inResponseToMessage) {
		super(status);
		setHeader(SpecialHeader.In_Response_To, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(int status, RequestMessage inResponseToMessage, CharSequence content) {
		this(status, inResponseToMessage);
		setContent(content);
	}

	@Override
	public void setContent(byte[] content) {
		super.setContent(content);
	}

	@Override
	public void setContent(CharSequence content) {
		super.setContent(content);
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(name, value);
	}
	
	
}
