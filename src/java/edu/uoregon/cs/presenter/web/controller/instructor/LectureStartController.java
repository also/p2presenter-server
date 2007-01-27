/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.LectureSession;
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
		int crn = ServletRequestUtils.getRequiredIntParameter(request, "crn");
		int lectureId = ServletRequestUtils.getRequiredIntParameter(request, "lectureId");
		
		Course course = getDao().getEntity(Course.class, crn);
		Lecture lecture = getDao().getEntity(Lecture.class, lectureId);
		
		LectureSession lectureSession = activeLectureController.newLectureSession(course, lecture);
		
		flashMessage("lectureSession.created", new Object[] {lectureSession, course, lecture}, "lecture session {0} created");
		return new ModelAndView("redirect:/instructor/control/" + lectureSession.getId());
	}

}
