/* $Id$ */

package edu.uoregon.cs.p2presenter.server.authentication;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.p2presenter.messaging.handler.RequestHandler;
import org.p2presenter.messaging.handler.RequestMatcher;
import org.p2presenter.messaging.message.IncomingRequestHeaders;
import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;


public class LoginRequestHandler implements RequestHandler {
	public static final RequestMatcher NOT_LOGIN_REQUEST_MATCHER = new RequestMatcher() {
		public boolean match(IncomingRequestHeaders incomingRequestHeaders) {
			return !"/login".equals(incomingRequestHeaders);
		}
	};
	
	private AuthenticationManager authenticationManager;
	
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		String[] usernamePassword = request.getContentAsString().split("(\r|\n|\r\n)");
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usernamePassword[0], usernamePassword[1]);
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextImpl securityContext = new SecurityContextImpl();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
		return new OutgoingResponseMessage(request);
	}

}
