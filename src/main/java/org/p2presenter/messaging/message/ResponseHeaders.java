/* $Id$ */

package org.p2presenter.messaging.message;

/** Headers that are available for response messages.
 * @author rberdeen
 *
 */
public interface ResponseHeaders extends Headers {
	public static final String IN_RESPONSE_TO = "In-Response-To";
	
	/** Return the status code.
	 */
	public int getStatus();
	
	/** Return the reason for the status code.
	 */
	public String getReason();
	
	/** Return the id of the message this is a response to.
	 */
	public String getInResponseTo();
}
