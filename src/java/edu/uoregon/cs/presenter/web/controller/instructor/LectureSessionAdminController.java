/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterController;

public class LectureSessionAdminController extends AbstractPresenterController {
	private ActiveLectureController activeLectureController;
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO this was copied verbatim from LectureSessionController
		Integer crn = ServletRequestUtils.getIntParameter(request, "crn");
		Integer lectureId = ServletRequestUtils.getIntParameter(request, "lectureId");
		ActiveLecture activeLecture;
		if (crn != null && lectureId != null) {
			activeLecture = activeLectureController.getActiveLecture(crn, lectureId);
		}
		else {
			int lectureSessionId = ServletRequestUtils.getRequiredIntParameter(request, "lectureSessionId");
			activeLecture = activeLectureController.getActiveLecture(lectureSessionId);
		}
		
		// TODO should require post
		Integer newCurrentSlideIndex = ServletRequestUtils.getIntParameter(request, "setCurrentSlideIndex");
		if (newCurrentSlideIndex != null) {
			activeLecture.setCurrentSlideIndex(newCurrentSlideIndex);
		}
		
		return new ModelAndView(getViewName(), "activeLecture", activeLecture);
	}

}
