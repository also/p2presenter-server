package org.p2presenter.messaging.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.p2presenter.messaging.message.IncomingRequestHeaders;


public class UriPatternRequestMatcher implements RequestMatcher {
	private Pattern pattern;
	private String[] parameterNames;
	
	public UriPatternRequestMatcher() {}
	
	public UriPatternRequestMatcher(String pattern, String... parameterNames) {
		setPattern(pattern);
		this.parameterNames = parameterNames;
	}
	
	public boolean match(IncomingRequestHeaders incomingRequestHeaders) {
		Matcher matcher = pattern.matcher(incomingRequestHeaders.getUri());
		if (matcher.matches()) {
			if (parameterNames != null) {
			int max = Math.min(matcher.groupCount(), parameterNames.length);
				for (int i = 0; i < max; i++) {
					incomingRequestHeaders.setAttribute(parameterNames[i], matcher.group(i + 1));
				}
			}
			
			return true;
		}
		return false;
	}
	
	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}
	
	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}
}
