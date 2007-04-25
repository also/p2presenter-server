/* $Id$ */

package org.p2presenter.messaging.handler;

import org.p2presenter.messaging.message.IncomingRequestHeaders;

public interface RequestHandlerMapping {
	public RequestHandler getHandler(IncomingRequestHeaders headers);
}
