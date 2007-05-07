/* $Id$ */

package edu.uoregon.cs.presenter.connector;

import java.io.File;
import java.io.FileInputStream;

import org.p2presenter.messaging.message.IncomingRequestMessage;
import org.p2presenter.messaging.message.OutgoingResponseMessage;
import org.p2presenter.server.model.Slide;

import edu.uoregon.cs.presenter.controller.FileController;

public class SlideRequestHandler extends AbstractEntityMultiActionRequestHandler<Slide> {
	private FileController fileController;
	
	public SlideRequestHandler() {
		super(Slide.class);
	}
	
	public void setFileController(FileController fileController) {
		this.fileController = fileController;
	}
	
	public OutgoingResponseMessage image(IncomingRequestMessage request, Slide slide) throws Exception {
		File slideFile = fileController.getImageFile(slide);
		byte[] slideBytes = new byte[(int) slideFile.length()];
		FileInputStream in = new FileInputStream(slideFile);
		in.read(slideBytes);
		
		OutgoingResponseMessage response = new OutgoingResponseMessage(request);
		response.setContent(slideBytes);
		return response;
	}

}
