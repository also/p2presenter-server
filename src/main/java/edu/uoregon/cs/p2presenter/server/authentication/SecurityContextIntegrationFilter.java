/* $Id$ */

package edu.uoregon.cs.p2presenter.server.authentication;

import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.p2presenter.messaging.Connection;
import org.p2presenter.messaging.handler.Filter;
import org.p2presenter.messaging.message.IncomingRequestMessage;

/** Synchronizes the security context of the {@link Connection} with the {@link SecurityContextHolder} for the scope of the request.
 * <p>At the beginning of the request, the security context holder is given the security context from the connection. After the request has been handled, if the the security context has been replaced, the connection is updated.</p> 
 * @author Ryan Berdeen
 *
 */
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
