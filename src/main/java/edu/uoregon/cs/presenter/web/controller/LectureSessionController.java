package edu.uoregon.cs.presenter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.p2presenter.server.model.InteractivityDefinition;
import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.SlideSession;
import org.p2presenter.server.model.Whiteboard;
import org.ry1.json.JsonObject;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ryanberdeen.routes.RouteRedirectView;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;

public class LectureSessionController extends AbstractPresenterController {
	private Log logger = LogFactory.getLog(LectureSessionController.class);
	private ActiveLectureController activeLectureController;

	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer lectureId = ServletRequestUtils.getIntParameter(request, "lectureId");
		ActiveLecture activeLecture;
		if (lectureId != null) {
			Lecture lecture = getDao().loadEntity(Lecture.class, lectureId);
			activeLecture = activeLectureController.getActiveLecture(lectureId);
			if (activeLecture == null) {
				flashMessage("lecture.session.inactive", new Object[] {lecture.getTitle()}, "lecture {0} is not active");
				return new ModelAndView(new RouteRedirectView("controller", "studentLecture", "id", lecture.getId()));
			}
		}
		else {
			int lectureSessionId = ServletRequestUtils.getRequiredIntParameter(request, "lectureSessionId");
			activeLecture = activeLectureController.getActiveLectureForSessionId(lectureSessionId);
		}
		SlideSession slideSession;
		Whiteboard whiteboard;

		if (ServletRequestUtils.getBooleanParameter(request, "update", false)) {
			int previousStateCount = ServletRequestUtils.getRequiredIntParameter(request, "stateCount");
			logger.info("Waiting for updates on LectureSession '" + activeLecture.getLectureSessionId() + ", stateCount '" + previousStateCount + "'");
			int newStateCount = activeLecture.waitForStateChange(previousStateCount);
			slideSession = activeLecture.getCurrentSlideSession();
			whiteboard = activeLecture.getCurrentWhiteboard();

			// FIXME
			InteractivityDefinition interactivityDefinition = activeLecture.getCurrentSlideSession().getSlide().getInteractivityDefinition();
			JsonObject json = getJson(activeLecture);
			if (interactivityDefinition != null) {
				json.set("currentInteractivityDefinitionId", interactivityDefinition.getId());
			}

			response.setHeader("X-JSON", json.toString());

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
		JsonObject json = new JsonObject(activeLecture, "stateCount", "currentWhiteboardId", "currentWhiteboardInkCount", "currentSlideSessionId", "currentSlideId", "currentSlideSessionInkCount");

		return json;
	}

}
