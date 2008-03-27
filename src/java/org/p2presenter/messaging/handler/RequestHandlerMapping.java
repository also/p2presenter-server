/* $Id$ */

package org.p2presenter.messaging.handler;

import org.p2presenter.messaging.message.IncomingRequestHeaders;

/** Maps message headers to handlers than can handle the message.
 * 
 * @author Ryan Berdeen
 *
 */
public interface RequestHandlerMapping {
	
	/** Locates a handler that can handle the messages associated with the headers.
	 * @param headers the headers to match against
	 * @return the handler if one matches, <code>null</code> otherwise
	 */
	public RequestHandler getHandler(IncomingRequestHeaders headers);
}
