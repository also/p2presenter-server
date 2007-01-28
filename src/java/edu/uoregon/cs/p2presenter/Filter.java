/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;

public interface Filter {
	public void filterRequest(IncomingRequestMessage request, Filter chain) throws Exception;
}
