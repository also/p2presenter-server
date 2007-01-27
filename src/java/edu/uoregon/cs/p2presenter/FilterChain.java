/* $Id$ */

package edu.uoregon.cs.p2presenter;

import java.util.Iterator;

import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;

public class FilterChain implements Filter, RequestHandler {
	private Iterator<Filter> filterIterator;
	private RequestHandler requestHandler;
	private OutgoingResponseMessage response;
	
	public FilterChain(Iterator<Filter> filterIterator, RequestHandler requestHandler) {
		this.filterIterator = filterIterator;
		this.requestHandler = requestHandler;
	}

	public void filterRequest(IncomingRequestMessage request, Filter chain) throws Exception {
		if (filterIterator.hasNext()) {
			filterIterator.next().filterRequest(request, chain);
		}
		else {
			response = requestHandler.handleRequest(request);
		}
	}

	public void filterResponse(IncomingResponseMessage response, Filter chain) throws Exception {
		if (filterIterator.hasNext()) {
			filterIterator.next().filterResponse(response, chain);
		}
	}

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		filterRequest(request, this);
		return response;
	}
	
}
