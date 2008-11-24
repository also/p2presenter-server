package org.p2presenter.web.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Course;
import org.p2presenter.web.common.AbstractEntityController;
import org.p2presenter.web.common.EntityController;
import org.springframework.web.servlet.ModelAndView;

@EntityController(entityClass = Course.class)
public class InstructorCourseController extends AbstractEntityController {
	public ModelAndView new_(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("instructor/course/create", "course", new Course());
	}

	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
		return null;
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
