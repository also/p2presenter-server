package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.manager.LectureManager;
import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.Slide;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;

import edu.uoregon.cs.presenter.web.controller.AbstractPresenterSimpleFormController;

public class NewSlideController extends AbstractPresenterSimpleFormController {
	LectureManager lectureManager;

	public void setLectureManager(LectureManager lectureManager) {
		this.lectureManager = lectureManager;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Lecture lecture = getDao().getEntity(Lecture.class, ServletRequestUtils.getRequiredIntParameter(request, "lectureId"));

		Slide slide = new Slide();
		slide.setIndex(lecture.getSlides().size());
		slide.setLecture(lecture);

		return new SlideFormBackingObject(slide);
	}

	@Override
	protected void doSubmitAction(Object command) throws Exception {
		SlideFormBackingObject slideFormBackingObject = (SlideFormBackingObject) command;

		Slide slide = slideFormBackingObject.getSlide();
		Lecture lecture = slide.getLecture();
		MultipartFile slideImage = slideFormBackingObject.getSlideImage();
		lectureManager.addSlide(lecture, slide, slideImage.getInputStream());
	}
}
