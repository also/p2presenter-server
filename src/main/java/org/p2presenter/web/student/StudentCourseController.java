package org.p2presenter.web.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Person;
import org.p2presenter.web.common.AbstractEntityController;
import org.p2presenter.web.common.EntityController;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ryanberdeen.routes.RouteRedirectView;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;

@EntityController(entityClass = Course.class)
public class StudentCourseController extends AbstractEntityController {
	private static final RouteRedirectView COURSE_LIST = new RouteRedirectView(
			"controller", "studentCourse",
			"action", "index"
	);


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

		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("person", person);
		model.put("activeLectures", activeLectures);
		return new ModelAndView("student/course/index", model);
	}

	public ModelAndView my(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("student/course/my");
	}

	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Course course = getEntity(request);

		String viewName;

		if (getPerson(request).getCoursesAttended().contains(course)) {
			viewName = "student/course/show";
		}
		else {
			viewName = "student/course/notEnrolled";
		}

		return new ModelAndView(viewName, "course", getEntity(request));
	}

	public ModelAndView enroll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String message = null;

		HashMap<String, Object> model = new HashMap<String, Object>();

		Person person = getPerson(request);
		Course course = getEntity(request);
		if (course == null) {
			message = "Invalid course";
		}
		else if (course.getStudents().contains(person)) {
			flashMessage("course.alreadyEnrolled", new Object[] {course}, "course {0} already enrolled");
			return new ModelAndView(COURSE_LIST);
		}

		if ("post".equalsIgnoreCase(request.getMethod()) && message == null) {
			person.getCoursesAttended().add(course);
			course.getStudents().add(person);
			getDao().flush();
			flashMessage("course.enrolled", new Object[] {course}, "course {0} enrolled");
			return new ModelAndView(new RouteRedirectView(
					"controller", "studentCourse",
					"action", "show",
					"id", course.getId()
			));
		}
		model.put("course", course);
		model.put("message", message);

		return new ModelAndView("student/course/enroll", model);
	}

	public ModelAndView drop(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Course course = getEntity(request);

		if ("post".equalsIgnoreCase(request.getMethod())) {
			Person person = getPerson(request);

			course.getStudents().remove(person);
			person.getCoursesAttended().remove(course);
			getDao().flush();
			flashMessage("course.dropped", new Object[] {course}, "course {0} dropped");
			return new ModelAndView(COURSE_LIST);
		}
		else {
			return new ModelAndView("student/course/drop", "course", course);
		}
	}

	public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer crn = ServletRequestUtils.getIntParameter(request, "crn");
		Course course = getDao().getCourseByCrn(crn);

		ArrayList<Course> courses = new ArrayList<Course>();
		if (course != null) {
			courses.add(course);
		}

		return new ModelAndView("student/course/search", "courses", courses);
	}

}
