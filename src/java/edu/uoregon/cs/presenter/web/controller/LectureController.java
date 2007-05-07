/* $Id:LectureController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Lecture;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLectureController;

public class LectureController extends AbstractPresenterController {
	private ActiveLectureController activeLectureController;
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Lecture lecture = getDao().getEntity(Lecture.class, ServletRequestUtils.getRequiredIntParameter(request, "lectureId"));
		ModelAndView result = new ModelAndView(getViewName(), "lecture", lecture);
		result.addObject("activeLecture", activeLectureController.getActiveLecture(lecture));
		return result;
	}

}
