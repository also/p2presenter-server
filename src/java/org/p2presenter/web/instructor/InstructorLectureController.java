package org.p2presenter.web.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.LectureSession;
import org.p2presenter.web.common.AbstractEntityController;
import org.p2presenter.web.common.EntityController;
import org.ry1.springframework.web.routes.RouteRedirectView;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLectureController;

@EntityController(entityClass = Lecture.class)
public class InstructorLectureController extends AbstractEntityController {
private ActiveLectureController activeLectureController;
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Lecture lecture = getEntity(request);
		ModelAndView result = new ModelAndView("instructor/lecture/show", "lecture", lecture);
		result.addObject("activeLecture", activeLectureController.getActiveLecture(lecture));
		return result;
	}
	
	public ModelAndView start(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO should require post
		
		Lecture lecture = getEntity(request);
		
		LectureSession lectureSession = activeLectureController.newLectureSession(lecture);
		
		flashMessage("lectureSession.created", new Object[] {lectureSession, lecture.getCourse(), lecture}, "lecture session {0} created");
		return new ModelAndView(new RouteRedirectView(
				"controller", "instructorLectureSession",
				"action", "control",
				"id", lectureSession.getId()
		));
	}
}
