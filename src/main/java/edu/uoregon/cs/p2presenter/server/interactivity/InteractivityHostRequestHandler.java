package edu.uoregon.cs.p2presenter.server.interactivity;

import org.p2presenter.server.model.InteractivityDefinition;
import org.ry1.json.JsonObject;

import com.ryanberdeen.postal.Connection;
import com.ryanberdeen.postal.ConnectionLifecycleListener;
import com.ryanberdeen.postal.handler.RequestHandler;
import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;

import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;
import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.security.AuthorizationUtils;

/** Handles request from interactivity hosts.
 * @author Ryan Berdeen
 *
 */
public class InteractivityHostRequestHandler implements RequestHandler, ConnectionLifecycleListener {
	private Dao dao;
	private ActiveInteractivityController activeInteractivityController;

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		// AUTHORIZATION
		// TODO more advanced check
		if (!AuthorizationUtils.hasRoles("ROLE_INSTRUCTOR")) {
			return new OutgoingResponseMessage(request, 400);
		}

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
