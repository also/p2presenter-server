package edu.uoregon.cs.p2presenter.interactivity;

import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionListener;
import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.presenter.controller.ActiveInteractivityController;
import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.entity.InteractivityDefinition;

public class ActiveInteractivityControllerRequestHandler implements RequestHandler, ConnectionListener {
	private Dao dao;
	private ActiveInteractivityController activeInteractivityController;
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public void setActiveInteractivityController(ActiveInteractivityController activeInteractivityController) {
		this.activeInteractivityController = activeInteractivityController;
	}
	
	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		// TODO don't use a header
		String action = request.getHeader("Action");
		int id = (Integer) request.getAttribute(InteractivityRequestMatcher.INTERACTIVITY_ID_ATTRIBUTE_NAME);
		
		InteractivityDefinition definition = dao.getEntity(InteractivityDefinition.class, id);
		
		if (action.equals("get")) {
			OutgoingResponseMessage response = new OutgoingResponseMessage(request);
			JsonObject result = new JsonObject(definition, "id", "hostControllerClassName");
			response.setContent(result.toString());
			return response;
		}
		else if (action.equals("begin")) {
			activeInteractivityController.addActiveInteractivity(id, new ActiveInteractivity(request.getConnection(), definition));
			return new OutgoingResponseMessage(request);
		}
		
		return null;
	}

	public void connectionClosed(Connection connection) {
		// TODO end interactivity
	}
}
