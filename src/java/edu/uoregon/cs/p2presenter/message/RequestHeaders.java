/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

/** Headers that are available for request messages.
 * @author rberdeen
 *
 */
public interface RequestHeaders extends Headers {
	public enum RequestType {
		GET,
		EVALUATE;
	}
	
	public static final String MESSAGE_ID = "Message-Id";
	
	/** Return the type of request, if one of the standard types.
	 */
	public RequestType getRequestType();
	
	/** Return the requested URI.
	 */
	public String getUri();
	
	/** Return the message id, if any.
	 */
	public String getMessageId();
}
