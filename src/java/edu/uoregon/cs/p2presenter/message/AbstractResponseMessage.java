/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

import edu.uoregon.cs.p2presenter.LocalConnection;

/** Superclass for response messages.
 * @author rberdeen
 *
 */
public abstract class AbstractResponseMessage extends AbstractMessage implements ResponseMessage {
	private int status;
	private String reason = "None";
	
	protected AbstractResponseMessage(int status) {
		this.status = status;
	}
	
	protected AbstractResponseMessage(int status, String reason) {
		this(status);
		this.reason = reason;
	}
	
	protected AbstractResponseMessage(AbstractResponseMessage that) {
		super(that);
		this.status = that.status;
		this.reason = that.reason;
	}
	
	public final int getStatus() {
		return status;
	}
	
	/** Sets the status of the message.
	 */
	protected void setStatus(int status) {
		this.status = status;
	}
	
	public final String getReason() {
		return reason;
	}
	
	/** Sets the reason for the message.
	 */
	protected void setReason(String reason) {
		this.reason = reason;
	}
	
	public final boolean isRequest() {
		return false;
	}
	
	public final String getInResponseTo() {
		return getHeader(IN_RESPONSE_TO);
	}

	@Override
	protected final String getStartLine() {
		return LocalConnection.PROTOCOL_VERSION + ' ' + getStatus() + ' ' + getReason();
	}
}
