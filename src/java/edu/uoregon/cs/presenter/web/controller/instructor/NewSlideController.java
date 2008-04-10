/* $Id$ */

package edu.uoregon.cs.presenter.web.controller.instructor;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.Slide;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;

import edu.uoregon.cs.presenter.controller.FileManager;
import edu.uoregon.cs.presenter.web.controller.AbstractPresenterSimpleFormController;

public class NewSlideController extends AbstractPresenterSimpleFormController {
	FileManager fileManager;
	
	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
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
		slide.setIndex(lecture.getSlides().size());
		lecture.getSlides().add(slide);
		
		getDao().save(slide);
		getDao().flush();
		
		MultipartFile slideImage = slideFormBackingObject.getSlideImage();
		if (slideImage != null) {
			slideImage.transferTo(fileManager.getImageFile(slide));
		}
		// TODO should indicate in slide that there is no image
	}
}
