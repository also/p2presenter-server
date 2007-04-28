/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Person;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterSimpleFormController;

public class CourseCreationController extends AbstractPresenterSimpleFormController {
	public CourseCreationController() {
		setCommandClass(Course.class);
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command) throws Exception {
		Course course = (Course) command;
		Person instructor = AbstractPresenterController.getPerson(request);  

		course.setInstructor(instructor);
	}
	
	@Override
	protected void doSubmitAction(Object command) throws Exception {
		Course course = (Course) command;
		getDao().save(course);
		course.getInstructor().getCoursesTaught().add(course);
		flashMessage("course.created", new Object[] {course, course.getCrn(), course.getSubject(), course.getNumber(), course.getTitle()}, "course {0} created");
	}

}
