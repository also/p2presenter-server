package edu.uoregon.cs.presenter.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.server.model.Slide;
import org.springframework.web.bind.ServletRequestUtils;


public class SlideImageController extends AbstractImageController {

	@Override
	protected File getImageFile(HttpServletRequest request) throws Exception {
		int id = ServletRequestUtils.getRequiredIntParameter(request, "id");
		if ("thumbnail".equals(request.getParameter("type"))) {
			return getFileManager().getFile("slideThumbnail", id);
		}
		else {
			return getFileManager().getImageFile(getDao().loadEntity(Slide.class, id));
		}
	}

}
