/* $Id$ */

package org.p2presenter.messaging.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.p2presenter.messaging.message.IncomingRequestHeaders;


public class DefaultRequestHandlerMapping implements RequestHandlerMapping {
	public static final String DEFAULT_URL = "/**";
	private Map<String, RequestHandler> uriMappedHandlers = new LinkedHashMap<String, RequestHandler>();
	private ArrayList<MatcherTuple<RequestHandler>> matcherMappedHandlers = new ArrayList<MatcherTuple<RequestHandler>>();
	private Map<String, List<Filter>> uriMappedFilters = new LinkedHashMap<String, List<Filter>>();
	private ArrayList<MatcherTuple<Filter>> matcherMappedFilters = new ArrayList<MatcherTuple<Filter>>();
	private RequestHandlerMapping parent;
	
	// TODO i think this should be a constructor argument
	public void setParent(RequestHandlerMapping parent) {
		this.parent = parent;
	}
	
	public RequestHandler getHandler(IncomingRequestHeaders headers) {
		RequestHandler result;
		if (parent != null) {
			result = parent.getHandler(headers);
			if (result != null) {
				return result;
			}
		}
		
		result = uriMappedHandlers.get(headers.getUri());
		
		if (result == null) {
			for (MatcherTuple<RequestHandler> matcherToHandler : matcherMappedHandlers) {
				if (matcherToHandler.requestMatcher.match(headers)) {
					result =  matcherToHandler.value;
				}
			}
		}
		if (result != null) {
			ArrayList<Filter> filters = new ArrayList<Filter>();
			buildFilterChain(headers, filters);
			if (filters.size() > 0) {
				result = new FilterChain(filters.iterator(), result);
			}
		}
		
		return result;
	}
	
	protected void buildFilterChain(IncomingRequestHeaders headers, ArrayList<Filter> filters) {
		if (parent != null) {
			((DefaultRequestHandlerMapping) parent).buildFilterChain(headers, filters);
		}
		List<Filter> mappedFilters = uriMappedFilters.get(DEFAULT_URL);
		if (mappedFilters != null) {
			filters.addAll(mappedFilters);
		}
		mappedFilters = uriMappedFilters.get(headers.getUri());
		if (mappedFilters != null) {
			filters.addAll(mappedFilters);
		}
		for (MatcherTuple<Filter> filterMatcher : matcherMappedFilters) {
			if (filterMatcher.requestMatcher.match(headers)) {
				filters.add(filterMatcher.value);
			}
		}
		
	}
	
	public void setUriMappedHandlers(Map<String, RequestHandler> uriMappedHandlers) {
		this.uriMappedHandlers = uriMappedHandlers;
	}
	
	public void mapHandler(String uri, RequestHandler handler) {
		uriMappedHandlers.put(uri, handler);
	}
	
	public void setMatcherMappedHandlers(Map<RequestMatcher, RequestHandler> matcherMappedHandlers) {
		for (Entry<RequestMatcher, RequestHandler> entry : matcherMappedHandlers.entrySet()) {
			mapHandler(entry.getKey(), entry.getValue());
		}
	}
	
	public void mapHandler(RequestMatcher requestMatcher, RequestHandler requestHandler) {
		matcherMappedHandlers.add(new MatcherTuple<RequestHandler>(requestMatcher, requestHandler));
	}
	
	public void setUriMappedFilters(Map<String, List<Filter>> uriMappedFilters) {
		this.uriMappedFilters = uriMappedFilters;
	}
	
	public void mapFilter(String uri, Filter filter) {
		List<Filter> filters = uriMappedFilters.get(uri);
		if (filters == null) {
			filters = new ArrayList<Filter>();
			uriMappedFilters.put(uri, filters);
		}
		filters.add(filter);
	}
	
	public void setMatcherMappedFilters(Map<RequestMatcher, Filter>  matcherMappedFilters) {
		for (Entry<RequestMatcher, Filter> entry : matcherMappedFilters.entrySet()) {
			mapFilter(entry.getKey(), entry.getValue());
		}
	}
	
	public void mapFilter(RequestMatcher requestMatcher, Filter filter) {
		matcherMappedFilters.add(new MatcherTuple<Filter>(requestMatcher, filter));
	}

	private static class MatcherTuple<T> {
		RequestMatcher requestMatcher;
		T value;
		
		public MatcherTuple(RequestMatcher requestMatcher, T value) {
			this.requestMatcher = requestMatcher;
			this.value = value;
		}
	}
}
