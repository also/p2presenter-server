/* $Id:HttpTransfers.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.up;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Lecture;
import org.p2presenter.server.model.Slide;
import org.p2presenter.server.model.SlideSession;
import org.p2presenter.server.model.Whiteboard;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.FileController;
import edu.uoregon.cs.presenter.dao.Dao;

public class HttpTransfers extends AbstractController {
	private Log logger = LogFactory.getLog(HttpTransfers.class);
	private Dao dao;
	private FileController fileController;
	
	private UbiquitousPresenterDao ubiquitousPresenterDao;
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public void setUbiquitousPresenterDao(UbiquitousPresenterDao ubiquitousPresenterDao) {
		this.ubiquitousPresenterDao = ubiquitousPresenterDao;
	}
	
	public void setFileController(FileController fileController) {
		this.fileController = fileController;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String type = request.getHeader("X-Type");

		String classroom = request.getHeader("X-Classroom");
		String lectureString = request.getHeader("X-Lecture");
		int slideIndex = Integer.parseInt(request.getHeader("X-Slideindex"));
		
		if (type.equals("base_slide")) {
			Course course = ubiquitousPresenterDao.getCourse(classroom);
			Lecture lecture = dao.getLectureByTitle(course, lectureString);
		
			List<Slide> slides = lecture.getSlides();
			
			if (slides.size() == slideIndex) {
				Slide slide = new Slide();
				slide.setIndex(slideIndex);
				slide.setLecture(lecture);
				slides.add(slide);
				dao.save(slide);
				File file = fileController.getImageFile(slide);
				saveFile(request, file);
			}
			else {
				// TODO insert the slide at the specified index
				logger.warn("Ignoring slide " + slideIndex + " recieved out of order");
			}
		}
		else if ("slide".equals(type)) {
			SlideType slideType = SlideType.values()[Integer.parseInt(request.getHeader("X-Slidetype"))];
			ActiveLecture activeLecture = ubiquitousPresenterDao.getActiveLecture(classroom, lectureString);

			if (activeLecture != null) {
				if (slideType == SlideType.SLIDE) {
					SlideSession slideSession = activeLecture.getCurrentSlideSession();
					if (slideSession != null) {
						int inkCount = activeLecture.slideInkAdded();
						File file = fileController.getImageFile(slideSession, inkCount - 1);
						saveFile(request, file);
						
						// TODO should be debug
						logger.info("Saving ink file '" + file + "'");
					}
					else {
						logger.warn("Recieved ink for slide " + slideIndex + " in inactive lecture '" + lectureString + "' in course '" + classroom);
					}
				
				}
				else if (slideType == SlideType.WHITEBOARD) {
					Whiteboard whiteboard = activeLecture.getCurrentWhiteboard();
					int inkCount = activeLecture.whiteboardInkAdded();
					File file = fileController.getImageFile(whiteboard, inkCount - 1);
					saveFile(request, file);
					
					// TODO should be debug
					logger.info("Saving ink file '" + file + "'");
				}
				else {
					logger.warn("Unsupported slide submission of type '" + slideType + "' ignored");
				}
			}
			else {
				logger.warn("Recieved ink for inactive lecture '" + lectureString + "' in classroom '" + classroom + "'");
			}
		}
		else {
			logger.warn("Unsupported slide of type '" + type + "' ignored");
		}
		return null;
	}
	
	private void saveFile(HttpServletRequest request, File file) throws Exception {
		ServletInputStream in = request.getInputStream();
		FileOutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		in.close();
		out.close();
	}

}
