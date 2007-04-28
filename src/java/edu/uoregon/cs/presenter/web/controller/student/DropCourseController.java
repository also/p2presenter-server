/* $Id:DropCourseController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.web.controller.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Person;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public class DropCourseController extends AbstractPresenterController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer courseId = ServletRequestUtils.getRequiredIntParameter(request, "courseId");
		Course course = getDao().getEntity(Course.class, courseId);
		
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
