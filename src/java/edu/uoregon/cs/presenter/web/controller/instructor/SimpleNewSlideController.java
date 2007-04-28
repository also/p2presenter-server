/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.Slide;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public class SimpleNewSlideController extends AbstractPresenterController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Lecture lecture = getDao().getEntity(Lecture.class, ServletRequestUtils.getRequiredIntParameter(request, "lectureId"));
		Slide slide = new Slide();
		slide.setIndex(lecture.getSlides().size());
		lecture.getSlides().add(slide);
		slide.setLecture(lecture);
		getDao().save(slide);
		getDao().flush();
		ModelAndView result = new ModelAndView(getViewName());
		return result;
	}

}
