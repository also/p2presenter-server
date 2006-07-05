/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

public interface RequestHandler {
	public OutgoingResponseMessage processRequest(IncomingRequestMessage request);
}
