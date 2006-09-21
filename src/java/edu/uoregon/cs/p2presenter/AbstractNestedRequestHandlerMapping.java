/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.Headers;

public abstract class AbstractNestedRequestHandlerMapping implements RequestHandlerMapping {

	public RequestHandler getHandler(Headers headers) {
		return null;
	}
	
	protected abstract RequestHandler getHandlerInternal(Headers headers);
}
