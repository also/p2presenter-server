/* $Id$ */

package edu.uoregon.cs.presenter.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import edu.uoregon.cs.presenter.entity.Slide;

public class SlideImageController extends AbstractImageController {

	@Override
	protected File getImageFile(HttpServletRequest request) throws Exception {
		return getFileController().getImageFile(getDao().loadEntity(Slide.class, ServletRequestUtils.getRequiredIntParameter(request, "slideId")));
	}

}
