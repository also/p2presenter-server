/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.jsh.JshRequestHandler;
import edu.uoregon.cs.p2presenter.message.IncomingRequestHeaders;

public class DefaultConnectionRequestHandlerMapping implements RequestHandlerMapping {
	private JshRequestHandler jshRequestHandler;
	
	public DefaultConnectionRequestHandlerMapping(Connection connection) {
		jshRequestHandler = new JshRequestHandler(connection);
	}
	
	public RequestHandler getHandler(IncomingRequestHeaders headers) {
		if ("bsh".equals(headers.getUrl())) {
			return jshRequestHandler;
		}
		else {
			return null;
		}
	}

}
