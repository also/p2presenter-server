/* $Id:DropCourseController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Person;

public class DropCourseController extends AbstractPresenterController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer crn = ServletRequestUtils.getRequiredIntParameter(request, "crn");
		Course course = getDao().getEntity(Course.class, crn);
		
		if ("post".equalsIgnoreCase(request.getMethod())) {
			Person person = getPerson(request);
			
			course.getStudents().remove(person);
			person.getCoursesAttended().remove(course);
			getDao().flush();
			flashMessage("course.dropped", new Object[] {course}, "course {0} dropped");
			return new ModelAndView("redirect:/courses");
		}
		else {
			return new ModelAndView(getViewName(), "course", course);
		}
	}

}
