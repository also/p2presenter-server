package org.p2presenter.web.instructor;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.p2presenter.web.common.AbstractEntityController;
import org.p2presenter.web.common.EntityController;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.ryanberdeen.routes.RouteRedirectView;

@EntityController(entityClass = Course.class)
public class InstructorCourseController extends AbstractEntityController {
	public ModelAndView new_(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("instructor/course/create", "course", new Course());
	}

	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
		Course course = new Course();
		course.setInstructor(getPerson(request));
		BindingResult bindingResult = bind(request, course, "course", "new");
		if (bindingResult.hasErrors()) {
			// TODO
			return new ModelAndView("instructor/course/create", "course", course);
		}
		else {
			Serializable id = getDao().save(course);
			flashMessage("course.created", new Object[] {course, course.getCrn(), course.getSubject(), course.getNumber(), course.getTitle()}, "course {0} created");
			return new ModelAndView(new RouteRedirectView(
				"controller", "instructorCourse",
				"id", id
			));
		}
	}

	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("instructor/course/index");
	}

	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("instructor/course/show", "course", getEntity(request));
	}

	public ModelAndView roster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("instructor/course/roster", "course", getEntity(request));
	}

	public ModelAndView my(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("instructor/course/my");
	}
}
