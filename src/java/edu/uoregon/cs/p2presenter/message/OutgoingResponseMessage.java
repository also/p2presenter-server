/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingResponseMessage extends ResponseMessageImpl {
	public OutgoingResponseMessage(int status, RequestMessage inResponseToMessage) {
		super(status);
		setHeader(SpecialHeader.In_Response_To, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(int status, RequestMessage inResponseToMessage, CharSequence content) {
		this(status, inResponseToMessage);
		setContent(content);
	}
}
