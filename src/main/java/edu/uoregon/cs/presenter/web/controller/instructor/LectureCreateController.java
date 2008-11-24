package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Lecture;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterSimpleFormController;

public class LectureCreateController extends AbstractPresenterSimpleFormController {
	private static String[] ALLOWED_FIELDS = {"title"};

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Lecture lecture = new Lecture();

		int courseId = ServletRequestUtils.getRequiredIntParameter(request, "courseId");
		lecture.setCourse(getDao().loadEntity(Course.class, courseId));

		lecture.setCreator(AbstractPresenterController.getPerson(request));

		return lecture;
	}

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.setAllowedFields(ALLOWED_FIELDS);
	}

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		Lecture lecture = (Lecture) command;

		getDao().save(lecture);
		// TODO maybe should add to course.lectures
		flashMessage("lecture.created", new Object[] {lecture}, "lecture {0} created");
	}
}
