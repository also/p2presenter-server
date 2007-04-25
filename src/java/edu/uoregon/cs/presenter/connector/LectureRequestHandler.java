/* $Id$ */

package edu.uoregon.cs.presenter.connector;

import org.ry1.json.JsonObject;
import org.ry1.json.PropertyList;

import edu.uoregon.cs.p2presenter.message.IncomingRequestMessage;
import edu.uoregon.cs.p2presenter.message.OutgoingResponseMessage;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.LectureSession;
import edu.uoregon.cs.presenter.entity.Person;
import edu.uoregon.cs.presenter.security.AuthorizationUtils;

public class LectureRequestHandler extends AbstractEntityMultiActionRequestHandler<Lecture> {
	private ActiveLectureController activeLectureController;
	
	private static final PropertyList LECTURE_PROPERTIES;
	private static final PropertyList LECTURE_SESSION_PROPERTIES;
	
	static {
		LECTURE_PROPERTIES = new PropertyList();
		LECTURE_PROPERTIES
			.includeValues("id", "title")
			.forListOfBeans("slides")
				.includeValues("id", "title", "body")
				.forBean("interactivityDefinition")
					.includeValue("id");
		
		LECTURE_SESSION_PROPERTIES = new PropertyList();
		LECTURE_SESSION_PROPERTIES
			.includeValue("id");
	}
	
	public LectureRequestHandler() {
		super(Lecture.class);
	}
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	@Override
	protected OutgoingResponseMessage afterEntityLoaded(IncomingRequestMessage request, Lecture lecture) {
		// AUTHORIZATION
		// only the creator of a lecture is allowed to access it here
		Person creator = lecture.getCreator();
		if (!creator.getUsername().equals(AuthorizationUtils.getCurrentUsername())) {
			return new OutgoingResponseMessage(request, 400);
		}
		else {
			return null;
		}
	}

	public OutgoingResponseMessage get(IncomingRequestMessage request, Lecture lecture) throws Exception {
		return new OutgoingResponseMessage(request, new JsonObject(lecture, LECTURE_PROPERTIES).toString());
	}
	
	public OutgoingResponseMessage begin(IncomingRequestMessage request, Lecture lecture) {
		// TODO this is a hack, assuming a lecture will only be in one course
		Course course = lecture.getCourses().iterator().next();
		LectureSession lectureSession = activeLectureController.newLectureSession(course, lecture);
		return new OutgoingResponseMessage(request, new JsonObject(lectureSession, LECTURE_SESSION_PROPERTIES).toString());
	}

}
