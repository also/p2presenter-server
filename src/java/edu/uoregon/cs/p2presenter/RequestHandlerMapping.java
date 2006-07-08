/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.RequestHeaders;

public interface RequestHandlerMapping {
	public RequestHandler getHandler(RequestHeaders headers);
}
