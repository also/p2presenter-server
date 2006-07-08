/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.jsh.JshRequestHandler;
import edu.uoregon.cs.p2presenter.message.RequestHeaders;

public class DefaultConnectionRequestHandlerMapping implements RequestHandlerMapping {
	private JshRequestHandler jshRequestHandler = new JshRequestHandler();
	
	public RequestHandler getHandler(RequestHeaders headers) {
		if ("bsh".equals(headers.getUrl())) {
			return jshRequestHandler;
		}
		else {
			return null;
		}
	}

}
