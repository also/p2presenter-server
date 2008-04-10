/* $Id:FileManager.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.controller;

import java.io.File;

import org.p2presenter.server.model.Slide;
import org.p2presenter.server.model.SlideSession;
import org.p2presenter.server.model.Whiteboard;


public class FileManager {
	private File baseDirectory = new File("/var/p2presenter");
	
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
