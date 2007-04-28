/* $Id:SubjectCourseListController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;


public class SubjectCourseListController extends AbstractPresenterController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subject = ServletRequestUtils.getRequiredStringParameter(request, "subject");
		
		List<Course> courses = getDao().getCoursesInSubject(subject);
		
		return new ModelAndView(getViewName(), "courses", courses);
	}

}
