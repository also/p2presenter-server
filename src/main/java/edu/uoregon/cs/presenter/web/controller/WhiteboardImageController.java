package edu.uoregon.cs.presenter.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.p2presenter.server.model.Whiteboard;
import org.springframework.web.bind.ServletRequestUtils;

public class WhiteboardImageController extends AbstractImageController {

	@Override
	protected File getImageFile(HttpServletRequest request) throws Exception {
		return getFileManager().getImageFile(getDao().loadEntity(Whiteboard.class, ServletRequestUtils.getRequiredIntParameter(request, "whiteboardId")));
	}

}
