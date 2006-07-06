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
	
	/** Return the type of request, if one of the standard types.
	 */
	public RequestType getRequestType();
	
	/** Return the requested URL.
	 */
	public String getUrl();
	
	/** Return the message id, if any.
	 */
	public String getMessageId();
}
