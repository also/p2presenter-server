/* $Id$ */

package edu.uoregon.cs.presenter.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.server.model.Slide;
import org.springframework.web.bind.ServletRequestUtils;


public class SlideImageController extends AbstractImageController {

	@Override
	protected File getImageFile(HttpServletRequest request) throws Exception {
		return getFileController().getImageFile(getDao().loadEntity(Slide.class, ServletRequestUtils.getRequiredIntParameter(request, "slideId")));
	}

}
