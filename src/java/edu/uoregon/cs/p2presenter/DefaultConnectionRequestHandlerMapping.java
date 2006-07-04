/* $Id$ */

package edu.uoregon.cs.p2presenter;

import edu.uoregon.cs.p2presenter.jsh.JshRequestHandler;
import edu.uoregon.cs.p2presenter.message.Headers;

public class DefaultConnectionRequestHandlerMapping implements RequestHandlerMapping {
	private JshRequestHandler jshRequestHandler = new JshRequestHandler();
	
	public RequestHandler getHandler(Headers headers) {
		return jshRequestHandler;
	}

}
