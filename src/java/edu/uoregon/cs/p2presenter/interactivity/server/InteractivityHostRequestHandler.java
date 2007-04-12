/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.server;

import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionListener;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.interactivity.ActiveInteractivity;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;
import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.entity.InteractivityDefinition;

/** Handles request from interactivity hosts.
 * @author Ryan Berdeen
 *
 */
public class InteractivityHostRequestHandler implements RequestHandler, ConnectionListener {
	private Dao dao;
	private ActiveInteractivityController activeInteractivityController;
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		String action = (String) request.getAttribute("action");
		Integer id = new Integer(request.getAttribute("interactivityId").toString());
		
		InteractivityDefinition definition = dao.getEntity(InteractivityDefinition.class, id);
		
		if (definition != null) {
			if (action.equals("get")) {
				OutgoingResponseMessage response = new OutgoingResponseMessage(request);
				JsonObject result = new JsonObject(definition, "id", "hostControllerClassName");
				response.setContent(result.toString());
				return response;
			}
			else if (action.equals("begin")) {
				activeInteractivityController.addActiveInteractivity(id, new ActiveInteractivity(request.getLocalConnection(), definition));
				return new OutgoingResponseMessage(request);
			}
		}
		
		return new OutgoingResponseMessage(request, 404);
	}

	public void connectionClosed(Connection connection) {
		// TODO end interactivity
	}
}
