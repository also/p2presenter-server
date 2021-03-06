package edu.uoregon.cs.presenter.web.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Person;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;

public class PersonController extends AbstractPresenterController {
	private ActiveLectureController activeLectureController;

	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Person person = getPerson(request);
		HashSet<ActiveLecture> activeLectures = new HashSet<ActiveLecture>();
		Set<Course> coursesAttended = person.getCoursesAttended();
		for (Course course : coursesAttended) {
			activeLectures.addAll(activeLectureController.getActiveLectures(course));
		}

		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("person", person);
		model.put("activeLectures", activeLectures);
		return new ModelAndView(getViewName(), model);
	}

}
