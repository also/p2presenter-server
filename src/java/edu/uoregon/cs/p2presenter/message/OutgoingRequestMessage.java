/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

public class OutgoingRequestMessage extends RequestMessageImpl {

	public OutgoingRequestMessage(String messageId) {
		setHeader(SpecialHeader.Message_Id, messageId);
	}
	
	public OutgoingRequestMessage(String messageId, CharSequence content) {
		this(messageId);
		setContent(content);
	}
	
	@Override
	public void setHeader(String name, String value) {
		super.setHeader(name, value);
	}

	@Override
	public void setContent(byte[] content) {
		super.setContent(content);
	}

	@Override
	public void setContent(CharSequence content) {
		super.setContent(content);
	}
}
