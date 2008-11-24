package edu.uoregon.cs.presenter.web.controller.instructor;

import org.p2presenter.server.model.Slide;
import org.springframework.web.multipart.MultipartFile;

public class SlideFormBackingObject {
	private Slide slide;
	private MultipartFile slideImage;

	public SlideFormBackingObject(Slide slide) {
		this.slide = slide;
	}

	public Slide getSlide() {
		return slide;
	}

	public MultipartFile getSlideImage() {
		return slideImage;
	}

	public void setSlideImage(MultipartFile slideImage) {
		this.slideImage = slideImage;
	}
}
