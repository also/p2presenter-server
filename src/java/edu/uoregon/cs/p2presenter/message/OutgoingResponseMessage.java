/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingResponseMessage extends AbstractResponseMessage implements OutgoingMessage {
	public OutgoingResponseMessage(RequestMessage inResponseToMessage) {
		this(inResponseToMessage, 200);
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, int status) {
		super(status);
		setHeader(SpecialHeader.In_Response_To, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, int status, CharSequence content) {
		this(inResponseToMessage, status);
		setContent(content);
	}
	
	@Override
	public void setStatus(int status) {
		super.setStatus(status);
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
	
	@Override
	public void setContentType(String contentType) {
		super.setContentType(contentType);
	}
}
