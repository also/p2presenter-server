/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.IncomingRequestHeaders;

public interface RequestHandlerMapping {
	public RequestHandler getHandler(IncomingRequestHeaders headers);
}
