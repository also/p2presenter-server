/* $Id:EnrollCourseController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.web.controller.student;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Person;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public class EnrollCourseController extends AbstractPresenterController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer courseId = null;
		String message = null;
	
		try {
			courseId = ServletRequestUtils.getIntParameter(request, "courseId");

		}
		catch (ServletRequestBindingException ex) {
			message = "Invalid course";
		}
		Course course = null;

		HashMap<String, Object> model = new HashMap<String, Object>();

		Person person = getPerson(request);
		if (courseId != null) {
			course = getDao().getEntity(Course.class, courseId);
			if (course == null) {
				message = "Invalid course";
			}
			else if (course.getStudents().contains(person)) {
				flashMessage("course.alreadyEnrolled", new Object[] {course}, "course {0} already enrolled");
				return new ModelAndView("redirect:/courses");
			}
		}

		if ("post".equalsIgnoreCase(request.getMethod()) && message == null) {
			person.getCoursesAttended().add(course);
			course.getStudents().add(person);
			getDao().flush();
			flashMessage("course.enrolled", new Object[] {course}, "course {0} enrolled");
			return new ModelAndView("redirect:/courses");
		}
		model.put("course", course);
		model.put("message", message);
		
		return new ModelAndView(getViewName(), model);
	}

}
