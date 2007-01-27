/* $Id:FileController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.controller;

import java.io.File;

import edu.uoregon.cs.presenter.entity.Slide;
import edu.uoregon.cs.presenter.entity.SlideSession;
import edu.uoregon.cs.presenter.entity.Whiteboard;

public class FileController {
	private File baseDirectory = new File(System.getProperty("user.home"), ".p2presenter/files");
	
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	public File getBaseDirectory() {
		return baseDirectory;
	}
	
	public File getImageFile(Slide slide) {
		return new File(baseDirectory, "slide-" + slide.getId() + ".png");
	}
	
	public File getImageFile(Whiteboard whiteboard) {
		return getImageFile(whiteboard, whiteboard.getInkCount() - 1);
	}
	
	public File getImageFile(SlideSession slideSession, int inkIndex) {
		return new File(baseDirectory, "slidesession-" + slideSession.getId() + "-ink-" + inkIndex + ".png");
	}
	
	public File getImageFile(Whiteboard whitebaord, int inkIndex) {
		return new File(baseDirectory, "whiteboard-" + whitebaord.getId() + "-ink-" + inkIndex + ".png");
	}
}