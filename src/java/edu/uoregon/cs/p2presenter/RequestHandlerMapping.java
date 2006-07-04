/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.Headers;

public interface RequestHandlerMapping {
	public RequestHandler getHandler(Headers headers);
}
