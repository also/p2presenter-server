/* $Id$ */

package org.p2presenter.messaging.handler;

import org.p2presenter.messaging.message.Headers;

public abstract class AbstractNestedRequestHandlerMapping implements RequestHandlerMapping {

	public RequestHandler getHandler(Headers headers) {
		return null;
	}
	
	protected abstract RequestHandler getHandlerInternal(Headers headers);
}
