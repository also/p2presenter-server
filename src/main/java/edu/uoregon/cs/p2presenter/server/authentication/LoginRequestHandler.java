package edu.uoregon.cs.p2presenter.server.authentication;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.ryanberdeen.postal.handler.RequestHandler;
import com.ryanberdeen.postal.handler.RequestMatcher;
import com.ryanberdeen.postal.message.IncomingRequestHeaders;
import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;

/** Logs users in.
 *
 * <p>The handler stores the {@link Authentication} returned by the {@link AuthenticationManager}
 * for the given <var>username</var> and <var>password</var>.</p>
 *
 * <p><strong>Expected message format:</strong></p>
 * <pre><var>username</var><kbd>\n</kbd><var>password</var></pre>
 *
 * @author Ryan Berdeen
 *
 */
public class LoginRequestHandler implements RequestHandler {
	public static final RequestMatcher NOT_LOGIN_REQUEST_MATCHER = new RequestMatcher() {
		public boolean match(IncomingRequestHeaders incomingRequestHeaders) {
			return !"/login".equals(incomingRequestHeaders);
		}
	};

	private AuthenticationManager authenticationManager;

	/** Sets the {@link AuthenticationManager} used to verify credentials.
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		String[] usernamePassword = request.getContentAsString().split("(\r|\n|\r\n)");
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usernamePassword[0], usernamePassword[1]);

		try {
			Authentication authentication = authenticationManager.authenticate(token);

			SecurityContextImpl securityContext = new SecurityContextImpl();
			securityContext.setAuthentication(authentication);
			SecurityContextHolder.setContext(securityContext);
			return new OutgoingResponseMessage(request);
		}
		catch (Exception ex) {
			return new OutgoingResponseMessage(request, 401, ex.getMessage());
		}
	}

}
