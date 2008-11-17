package org.p2presenter.web.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Lecture;
import org.p2presenter.web.common.AbstractEntityController;
import org.p2presenter.web.common.EntityController;
import org.springframework.web.servlet.ModelAndView;

@EntityController(entityClass = Lecture.class)
public class StudentLectureController extends AbstractEntityController {
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Lecture lecture = getEntity(request);

		return new ModelAndView("student/lecture/show", "lecture", lecture);
	}
}
