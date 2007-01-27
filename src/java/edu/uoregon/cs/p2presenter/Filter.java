/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;

public interface Filter {
	public void filterRequest(IncomingRequestMessage request, Filter chain) throws Exception;
	
	public void filterResponse(IncomingResponseMessage response, Filter chain) throws Exception;
}
