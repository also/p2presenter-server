/* $Id$ */

package edu.uoregon.cs.presenter.connector;

import java.io.File;
import java.io.FileInputStream;

import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;
import org.p2presenter.server.model.Slide;

import edu.uoregon.cs.presenter.controller.FileManager;

public class SlideRequestHandler extends AbstractEntityMultiActionRequestHandler<Slide> {
	private FileManager fileManager;
	
	public SlideRequestHandler() {
		super(Slide.class);
	}
	
	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}
	
	public OutgoingResponseMessage image(IncomingRequestMessage request, Slide slide) throws Exception {
		File slideFile = fileManager.getImageFile(slide);
		byte[] slideBytes = new byte[(int) slideFile.length()];
		FileInputStream in = new FileInputStream(slideFile);
		in.read(slideBytes);
		
		OutgoingResponseMessage response = new OutgoingResponseMessage(request);
		response.setContent(slideBytes);
		return response;
	}

}
