/* $Id$ */

package org.p2presenter.messaging.message;

public class OutgoingResponseMessage extends AbstractResponseMessage implements OutgoingMessage {
	private static final int DEFAULT_STATUS = 200;
	
	/** Constructs a response message with the default status code 200.
	 * @param inResponseToMessage
	 */
	public OutgoingResponseMessage(RequestMessage inResponseToMessage) {
		this(inResponseToMessage, DEFAULT_STATUS);
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, int status) {
		super(status);
		setHeader(IN_RESPONSE_TO, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, CharSequence content) {
		this(inResponseToMessage, DEFAULT_STATUS);
		setContent(content);
		setHeader(IN_RESPONSE_TO, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, int status, CharSequence content) {
		this(inResponseToMessage, status);
		setContent(content);
		setHeader(IN_RESPONSE_TO, inResponseToMessage.getMessageId());
	}
	
	public OutgoingResponseMessage(RequestMessage inResponseToMessage, AbstractResponseMessage that) {
		super(that);
		setHeader(IN_RESPONSE_TO, inResponseToMessage.getMessageId());
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
