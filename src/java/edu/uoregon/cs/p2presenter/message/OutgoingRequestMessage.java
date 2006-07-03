/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingRequestMessage extends RequestMessageImpl implements OutgoingMessage {

	public OutgoingRequestMessage(String messageId) {
		if (containsLineEnd(messageId)) {
			throw new IllegalArgumentException("End of line in message id");
		}
		setHeader(SpecialHeader.Message_Id, messageId);
	}
	
	public OutgoingRequestMessage(String messageId, byte[] content) {
		this(messageId);
		setContent(content);
	}
	
	public OutgoingRequestMessage(String messageId, CharSequence content) {
		this(messageId);
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
