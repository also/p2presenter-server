/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.LectureSession;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public class LectureStartController extends AbstractPresenterController {
	private ActiveLectureController activeLectureController;
	
	public void setActiveLectureController(
			ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO should require post
		int courseId = ServletRequestUtils.getRequiredIntParameter(request, "courseId");
		int lectureId = ServletRequestUtils.getRequiredIntParameter(request, "lectureId");
		
		Course course = getDao().getEntity(Course.class, courseId);
		Lecture lecture = getDao().getEntity(Lecture.class, lectureId);
		
		LectureSession lectureSession = activeLectureController.newLectureSession(lecture);
		
		flashMessage("lectureSession.created", new Object[] {lectureSession, course, lecture}, "lecture session {0} created");
		return new ModelAndView("redirect:/instructor/control/" + lectureSession.getId());
	}

}
