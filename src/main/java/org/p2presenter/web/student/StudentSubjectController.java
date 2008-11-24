package org.p2presenter.web.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public class StudentSubjectController extends AbstractPresenterController {
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("student/subject/index", "subjects", getDao().getSubjects());
	}

	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subject = ServletRequestUtils.getRequiredStringParameter(request, "subject");

		List<Course> courses = getDao().getCoursesInSubject(subject);

		return new ModelAndView("student/subject/show", "courses", courses);
	}
}
