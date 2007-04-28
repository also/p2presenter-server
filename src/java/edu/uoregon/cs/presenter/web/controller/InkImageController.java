/* $Id$ */

package edu.uoregon.cs.presenter.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.server.model.SlideSession;
import org.springframework.web.bind.ServletRequestUtils;


public class InkImageController extends AbstractImageController {

	@Override
	protected File getImageFile(HttpServletRequest request) throws Exception {
		return getFileController().getImageFile(getDao().getEntity(SlideSession.class, ServletRequestUtils.getRequiredIntParameter(request, "slideSessionId")), ServletRequestUtils.getRequiredIntParameter(request, "inkIndex"));
	}

}
