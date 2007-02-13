/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.instructor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;

import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.Person;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterSimpleFormController;

public class LectureCreateController extends AbstractPresenterSimpleFormController {
	private static String[] ALLOWED_FIELDS = {"title"};
	public LectureCreateController() {
		setCommandClass(Lecture.class);
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.setAllowedFields(ALLOWED_FIELDS);
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command) throws Exception {
		Lecture lecture = (Lecture) command;
		Person person = AbstractPresenterController.getPerson(request);
		lecture.setCreator(person);
		Course course = (Course) request.getAttribute("course");
		lecture.getCourses().add(course);
	}
	
	@Override
	protected void doSubmitAction(Object command) throws Exception {
		Lecture lecture = (Lecture) command;
		
		getDao().save(lecture);
		// TODO maybe should add to course.lectures
		flashMessage("lecture.created", new Object[] {lecture}, "lecture {0} created");
	}

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();

		int courseId = ServletRequestUtils.getRequiredIntParameter(request, "courseId");
		result.put("course", getDao().loadEntity(Course.class, courseId));
		
		return result;
	}
	
	

}
