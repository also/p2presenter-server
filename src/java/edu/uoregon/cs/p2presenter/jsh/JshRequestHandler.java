/* $Id$ */

package edu.uoregon.cs.p2presenter.jsh;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.ParseException;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.p2presenter.message.RequestMessage;

public class JshRequestHandler implements RequestHandler {
	
	private Interpreter interpreter = new Interpreter();
	
	public OutgoingResponseMessage handleRequest(RequestMessage request) {
		OutgoingResponseMessage response = new OutgoingResponseMessage(request);
		try {
			if (request.hasContent()) {
				Object result = interpreter.eval(request.getContentAsString());
				if (result != null) {
					response.setContent(result.toString());
				}
			}
		}
		catch (ParseException ex) {
			response.setStatus(400);
			response.setContent(ex.getMessage());
		}
		catch (EvalError ex) {
			response.setStatus(500);
			response.setContent(ex.getMessage());
		}
		return response;
	}
}
