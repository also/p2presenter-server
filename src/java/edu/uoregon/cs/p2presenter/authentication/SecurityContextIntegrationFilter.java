/* $Id$ */

package edu.uoregon.cs.p2presenter.authentication;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

import edu.uoregon.cs.p2presenter.Filter;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.IncomingResponseMessage;

public class SecurityContextIntegrationFilter implements Filter {
	public static final String SECURITY_CONTEXT_ATTRIBUTE_NAME = SecurityContextIntegrationFilter.class + ".securityContext";
	
	public void filterRequest(IncomingRequestMessage request, Filter chain) throws Exception {
		SecurityContext securityContext = (SecurityContext) request.getAttribute(SECURITY_CONTEXT_ATTRIBUTE_NAME);
		if (securityContext != null) {
			SecurityContextHolder.setContext(securityContext);
		}
		else {
			SecurityContextHolder.clearContext();
		}
		try {
			chain.filterRequest(request, chain);
		}
		finally {
			SecurityContextHolder.clearContext();
		}
	}

	public void filterResponse(IncomingResponseMessage response, Filter chain) throws Exception {
		chain.filterResponse(response, chain);
	}

}
