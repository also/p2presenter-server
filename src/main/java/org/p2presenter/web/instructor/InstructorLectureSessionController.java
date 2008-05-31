package org.p2presenter.web.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.LectureSession;
import org.p2presenter.web.common.AbstractEntityController;
import org.p2presenter.web.common.EntityController;
import org.ry1.springframework.web.routes.RouteRedirectView;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;

@EntityController(entityClass = LectureSession.class)
public class InstructorLectureSessionController extends AbstractEntityController {
	private ActiveLectureController activeLectureController;
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("instructor/lectureSession/show", "lectureSession", getEntity(request));
	}
	
	public ModelAndView control(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO this was copied verbatim from LectureSessionController
		Integer courseId = ServletRequestUtils.getIntParameter(request, "courseId");
		Integer lectureId = ServletRequestUtils.getIntParameter(request, "lectureId");
		
		ActiveLecture activeLecture;
		if (courseId != null && lectureId != null) {
			activeLecture = activeLectureController.getActiveLecture(lectureId);
		}
		else {
			int lectureSessionId = ServletRequestUtils.getRequiredIntParameter(request, "id");
			activeLecture = activeLectureController.getActiveLectureForSessionId(lectureSessionId);
		}
		
		if (activeLecture == null) {
			flashMessage("lectureSession.ended", null, "the lecture session has ended");
			LectureSession lectureSession = getEntity(request);
			return new ModelAndView(new RouteRedirectView("controller", "instructorLecture", "id", lectureSession.getLecture().getId()));
		}
		
		// TODO should require post
		Integer newCurrentSlideIndex = ServletRequestUtils.getIntParameter(request, "setCurrentSlideIndex");
		if (newCurrentSlideIndex != null) {
			activeLecture.setCurrentSlideIndex(newCurrentSlideIndex);
		}
		
		return new ModelAndView("instructor/lectureSession/control", "activeLecture", activeLecture);
	}

}
