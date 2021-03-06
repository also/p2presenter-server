package edu.uoregon.cs.presenter.connector;

import org.p2presenter.server.model.LectureSession;
import org.ry1.json.JsonObject;
import org.ry1.json.PropertyList;

import com.ryanberdeen.postal.message.IncomingRequestMessage;
import com.ryanberdeen.postal.message.OutgoingResponseMessage;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;

public class ActiveLectureRequestHandler extends AbstractEntityMultiActionRequestHandler<LectureSession> {

	private static final PropertyList LECTURE_SESSION_PROPERTIES;

	static {
		LECTURE_SESSION_PROPERTIES = new PropertyList();
		LECTURE_SESSION_PROPERTIES
			.includeValue("id");
	}

	private ActiveLectureController activeLectureController;

	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}

	public ActiveLectureRequestHandler() {
		super(LectureSession.class);
	}

	public OutgoingResponseMessage setCurrentSlideIndex(IncomingRequestMessage request, LectureSession lectureSession) {
		Integer index = new Integer(request.getContentAsString());
		activeLectureController.getActiveLectureForSessionId(lectureSession.getId()).setCurrentSlideIndex(index);

		return new OutgoingResponseMessage(request, new JsonObject(lectureSession, LECTURE_SESSION_PROPERTIES).toString());
	}

	public OutgoingResponseMessage end(IncomingRequestMessage request, LectureSession lectureSession) {
		ActiveLecture activeLecture = activeLectureController.getActiveLectureForSessionId(lectureSession.getId());
		activeLectureController.endActiveLecture(activeLecture);

		return new OutgoingResponseMessage(request, new JsonObject(lectureSession, LECTURE_SESSION_PROPERTIES).toString());
	}

}
