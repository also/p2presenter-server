/* $Id$ */

package edu.uoregon.cs.presenter.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import edu.uoregon.cs.presenter.entity.Whiteboard;

public class WhiteboardImageController extends AbstractImageController {

	@Override
	protected File getImageFile(HttpServletRequest request) throws Exception {
		return getFileController().getImageFile(getDao().loadEntity(Whiteboard.class, ServletRequestUtils.getRequiredIntParameter(request, "whiteboardId")));
	}

}
