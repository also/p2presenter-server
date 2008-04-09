package org.p2presenter.web;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Person;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public class DashboardController extends AbstractPresenterController {
	private ActiveLectureController activeLectureController;
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Person person = getPerson(request);
		HashSet<ActiveLecture> activeLectures = new HashSet<ActiveLecture>();
		Set<Course> coursesAttended = person.getCoursesAttended();
		for (Course course : coursesAttended) {
			activeLectures.addAll(activeLectureController.getActiveLectures(course));
		}
		
		return new ModelAndView("dashboard/index", "activeLectures", activeLectures);
	}
}
