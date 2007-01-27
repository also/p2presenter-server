/* $Id$ */

package edu.uoregon.cs.presenter.web.controller;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.uoregon.cs.presenter.controller.FileController;
import edu.uoregon.cs.presenter.dao.Dao;

public abstract class AbstractImageController extends AbstractController {
	private static final int BUFFER_SIZE = 1024;
	private Dao dao;
	private FileController fileController;
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	protected Dao getDao() {
		return dao;
	}
	
	public void setFileController(FileController fileController) {
		this.fileController = fileController;
	}
	
	protected FileController getFileController() {
		return fileController;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		File file = getImageFile(request);
		
		response.setContentType("image/png");
		response.setContentLength((int) file.length());
		
		FileInputStream in = new FileInputStream(file);
		
		try {
			ServletOutputStream out = response.getOutputStream();
			
			byte[] buffer = new byte[BUFFER_SIZE];
			while(in.read(buffer) > 0) {
				out.write(buffer);
			}
		}
		finally {
			in.close();
		}
		
		return null;
	}
	
	protected abstract File getImageFile(HttpServletRequest request) throws Exception;

}
