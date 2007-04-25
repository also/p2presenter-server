/* $Id$ */

package edu.uoregon.cs.p2presenter.server.authentication;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.p2presenter.messaging.handler.Filter;
import org.p2presenter.messaging.message.IncomingRequestMessage;


public class SecurityContextIntegrationFilter implements Filter {
	public static final String SECURITY_CONTEXT_ATTRIBUTE_NAME = SecurityContextIntegrationFilter.class.getName() + ".securityContext";
	
	public void filterRequest(IncomingRequestMessage request, Filter chain) throws Exception {
		SecurityContext securityContext = (SecurityContext) request.getConnection().getAttribute(SECURITY_CONTEXT_ATTRIBUTE_NAME);
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
			SecurityContext newSecurityContext = SecurityContextHolder.getContext();
			if (newSecurityContext != securityContext) {
				request.getConnection().setAttribute(SECURITY_CONTEXT_ATTRIBUTE_NAME, newSecurityContext);
			}
			SecurityContextHolder.clearContext();
		}
	}
}
