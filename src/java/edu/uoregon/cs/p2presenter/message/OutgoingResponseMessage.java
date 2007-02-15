/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingResponseMessage extends AbstractResponseMessage implements OutgoingMessage {
	/** Constructs a response message with the default status code 200.
	 * @param inResponseToMessage
	 */
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
		setHeader(SpecialHeader.In_Response_To, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, AbstractResponseMessage that) {
		super(that);
		setHeader(SpecialHeader.In_Response_To, inResponseToMessage.getMessageId());
	}
	
	@Override
	public final void setStatus(int status) {
		super.setStatus(status);
	}

	@Override
	public  void setContent(byte[] content) {
		super.setContent(content);
	}

	@Override
	public final void setContent(CharSequence content) {
		super.setContent(content);
	}

	@Override
	public final void setHeader(String name, String value) {
		super.setHeader(name, value);
	}
	
	@Override
	public final void setContentType(String contentType) {
		super.setContentType(contentType);
	}
}
