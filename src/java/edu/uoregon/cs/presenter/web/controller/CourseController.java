/* $Id:CourseController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.entity.Course;

public class CourseController extends AbstractPresenterController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer crn = ServletRequestUtils.getRequiredIntParameter(request, "crn");
		return new ModelAndView(getViewName(), "course", getDao().getEntity(Course.class, crn));
	}

}
