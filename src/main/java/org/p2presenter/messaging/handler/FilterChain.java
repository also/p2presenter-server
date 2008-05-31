/* $Id$ */

package org.p2presenter.messaging.handler;

import java.util.Iterator;

import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;


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

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		filterRequest(request, this);
		return response;
	}
	
}
