/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestMessage;

public interface RequestProcessor {
	public OutgoingResponseMessage processRequest(RequestMessage request);
}
