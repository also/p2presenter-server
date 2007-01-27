/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uoregon.cs.p2presenter.RequestMatcher;
import edu.uoregon.cs.p2presenter.message.IncomingRequestHeaders;

public class InteractivityRequestMatcher implements RequestMatcher {
	/** The request attribute the interactivity id will be stored in. */
	public static final String INTERACTIVITY_ID_ATTRIBUTE_NAME = "interactivityId";
	public static final String URI_PREFIX = "/interactivity/";
	private Pattern pattern;
	
	public InteractivityRequestMatcher() {}
	
	public InteractivityRequestMatcher(String path) {
		setPath(path);
	}
	
	public void setPath(String path) {
		pattern = Pattern.compile(URI_PREFIX + "(\\d+)/" + path);
	}

	public boolean match(IncomingRequestHeaders incomingRequestHeaders) {
		Matcher matcher = pattern.matcher(incomingRequestHeaders.getUri());
		
		if (matcher.matches()) {
			incomingRequestHeaders.setAttribute(INTERACTIVITY_ID_ATTRIBUTE_NAME, new Integer(matcher.group(1)));
			return true;
		}
		else {
			return false;
		}
	}

}
