/* $Id$ */

package edu.uoregon.cs.presenter.connector;

import java.io.FileOutputStream;
import java.util.List;

import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;
import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.LectureSession;
import org.p2presenter.server.model.Slide;
import org.ry1.json.JsonObject;
import org.ry1.json.PropertyList;

import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.controller.FileController;

public class LectureRequestHandler extends AbstractEntityMultiActionRequestHandler<Lecture> {
	private ActiveLectureController activeLectureController;
	private FileController fileController;
	
	public static final PropertyList LECTURE_PROPERTIES;
	private static final PropertyList LECTURE_SESSION_PROPERTIES;
	
	static {
		LECTURE_PROPERTIES = new PropertyList();
		LECTURE_PROPERTIES
			.includeValues("id", "title")
			.forListOfBeans("slides")
				.includeValues("id", "index", "title", "body")
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
	
	public void setFileController(FileController fileController) {
		this.fileController = fileController;
	}
	
	@Override
	protected OutgoingResponseMessage afterEntityLoaded(IncomingRequestMessage request, Lecture lecture) {
		// AUTHORIZATION
		// only the creator of a lecture is allowed to access it here
		// FIXME disabled because lectures created by UP don't have creator set
		return null;
		/*Person creator = lecture.getCreator();
		if (!creator.getUsername().equals(AuthorizationUtils.getCurrentUsername())) {
			return new OutgoingResponseMessage(request, 400);
		}
		else {
			return null;
		}*/
	}

	public OutgoingResponseMessage get(IncomingRequestMessage request, Lecture lecture) throws Exception {
		return new OutgoingResponseMessage(request, new JsonObject(lecture, LECTURE_PROPERTIES).toString());
	}
	
	public OutgoingResponseMessage begin(IncomingRequestMessage request, Lecture lecture) {
		LectureSession lectureSession = activeLectureController.newLectureSession(lecture);
		return new OutgoingResponseMessage(request, new JsonObject(lectureSession, LECTURE_SESSION_PROPERTIES).toString());
	}
	
	public OutgoingResponseMessage addSlideImage(IncomingRequestMessage request, Lecture lecture) throws Exception {
		Slide slide = new Slide();
		List<Slide> slides = lecture.getSlides();
		slide.setIndex(slides.size());
		slide.setLecture(lecture);
		slides.add(slide);
		getDao().save(slide);
		
		// TODO stream in the slide
		FileOutputStream out = new FileOutputStream(fileController.getImageFile(slide));
		out.write(request.getContent());

		out.close();
		
		return new OutgoingResponseMessage(request);
	}

}
