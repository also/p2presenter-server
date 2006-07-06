/* $Id$ */

package edu.uoregon.cs.p2presenter.message;

/** Headers that are available for response messages.
 * @author rberdeen
 *
 */
public interface ResponseHeaders {
	
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
