/* $Id$ */

package edu.uoregon.cs.presenter.connector;

import org.ry1.json.JsonArray;
import org.ry1.json.JsonObject;

import edu.uoregon.cs.p2presenter.RequestHandler;
import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.entity.InteractivityDefinition;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.Slide;

public class LectureRequestHandler implements RequestHandler {
	private Dao dao;
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception {
		String action = (String) request.getAttribute("action");
		
		String idString = (String) request.getAttribute("lectureId");
		Integer id = idString != null ? new Integer(idString) : null;
		
		Lecture lecture = dao.getEntity(Lecture.class, id);
		
		if (action.equals("get")) {
			JsonObject result = new JsonObject(lecture, new String[] {"id", "title"});
			JsonArray slides = new JsonArray();
			for (Slide slide : lecture.getSlides()) {
				JsonObject slideJson = new JsonObject(slide, new String[] {"id", "title", "body"});
				InteractivityDefinition interactivityDefinition = slide.getInteractivityDefinition();
				if (interactivityDefinition != null) {
					slideJson.set("interactivityDefinition", new JsonObject(interactivityDefinition, new String[] {"id"}));
				}
				slides.addValue(slideJson);
				
			}
			
			result.set("slides", slides);
			
			OutgoingResponseMessage response = new OutgoingResponseMessage(request);
			response.setContent(result.toString());
			
			return response;
		}
		
		return new OutgoingResponseMessage(request, 404);
	}

}
