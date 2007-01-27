/* $Id:PersonController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.web.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Person;

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
