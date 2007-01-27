/* $Id:LectureSessionController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ry1.json.JsonObject;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.SlideSession;
import edu.uoregon.cs.presenter.entity.Whiteboard;

public class LectureSessionController extends AbstractPresenterController {
	private Log logger = LogFactory.getLog(LectureSessionController.class);
	private ActiveLectureController activeLectureController;
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer crn = ServletRequestUtils.getIntParameter(request, "crn");
		Integer lectureId = ServletRequestUtils.getIntParameter(request, "lectureId");
		ActiveLecture activeLecture;
		if (crn != null && lectureId != null) {
			Course course = getDao().loadEntity(Course.class, crn);
			Lecture lecture = getDao().loadEntity(Lecture.class, lectureId);
			activeLecture = activeLectureController.getActiveLecture(crn, lectureId);
			if (activeLecture == null) {
				flashMessage("lecture.session.inactive", new Object[] {lecture, course}, "lecture {0} is not active in course {1}");
				return new ModelAndView("redirect:/courses/" + crn + "/lectures/" + lectureId + '/');
			}
		}
		else {
			int lectureSessionId = ServletRequestUtils.getRequiredIntParameter(request, "lectureSessionId");
			activeLecture = activeLectureController.getActiveLecture(lectureSessionId);
		}
		SlideSession slideSession;
		Whiteboard whiteboard;
		
		if (ServletRequestUtils.getBooleanParameter(request, "update", false)) {
			int previousStateCount = ServletRequestUtils.getRequiredIntParameter(request, "stateCount");
			logger.info("Waiting for updates on LectureSession '" + activeLecture.getLectureSessionId() + ", stateCount '" + previousStateCount + "'");
			int newStateCount = activeLecture.waitForStateChange(previousStateCount);
			slideSession = activeLecture.getCurrentSlideSession();
			whiteboard = activeLecture.getCurrentWhiteboard();
			
			response.setHeader("X-JSON", getJson(activeLecture).toString());
			
			logger.info("Updated to state " + newStateCount);
			
			// safari is not so clever
			response.getWriter().write(" ");
			return null;
		}
		
		slideSession = activeLecture.getCurrentSlideSession();
		whiteboard = activeLecture.getCurrentWhiteboard();
		
		ModelAndView result = new ModelAndView(getViewName());
		// FIXME use actual state count
		result.addObject("json", getJson(activeLecture));
		result.addObject("activeLecture", activeLecture);
		result.addObject("currentSlideSession", slideSession);
		result.addObject("currentWhiteboard", whiteboard);
		result.addObject("currentStateCount", activeLecture.getStateCount());
		
		return result;
	}
	
	private JsonObject getJson(ActiveLecture activeLecture) {
		JsonObject json = new JsonObject();
		json.set("stateCount", activeLecture.getStateCount());
		Integer currentWhiteboardId = activeLecture.getCurrentWhiteboardId();
		Integer currentSlideSessionId = activeLecture.getCurrentSlideSessionId();
		if (currentWhiteboardId != null) {
			json.set("currentWhiteboardId", currentWhiteboardId);
			json.set("inkCount", activeLecture.getCurrentWhiteboardInkCount());
		}
		else if (currentSlideSessionId != null) {
			json.set("currentSlideSessionId", currentSlideSessionId);
			json.set("currentSlideId", activeLecture.getCurrentSlideId());
			json.set("inkCount", activeLecture.getCurrentSlideSessionInkCount());
		}
		
		return json;
	}

}
