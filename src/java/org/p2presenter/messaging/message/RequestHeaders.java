/* $Id$ */

package org.p2presenter.messaging.message;

/** Headers that are available for request messages.
 * @author rberdeen
 *
 */
public interface RequestHeaders extends Headers {
	@Deprecated
	public interface RequestType {
		public static String GET = "GET";
		public static String EVALUATE = "EVALUATE";
	}
	
	public static final String MESSAGE_ID = "Message-Id";
	
	/** Return the type of request.
	 */
	public String getRequestType();
	
	/** Return the requested URI.
	 */
	public String getUri();
	
	/** Return the message id, if any.
	 */
	public String getMessageId();
}
