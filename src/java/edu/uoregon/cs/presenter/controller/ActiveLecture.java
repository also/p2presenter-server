package edu.uoregon.cs.presenter.controller;

import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.LectureSession;
import edu.uoregon.cs.presenter.entity.Slide;
import edu.uoregon.cs.presenter.entity.SlideSession;
import edu.uoregon.cs.presenter.entity.Whiteboard;

public class ActiveLecture {
	private Dao dao;
	private ActiveLectureInfo activeLectureInfo;
	
	private LectureSession lectureSession;
	private Course course;
	private Lecture lecture;
	
	ActiveLecture(Dao dao, ActiveLectureInfo activeLectureInfo) {
		this.dao = dao;
		this.activeLectureInfo = activeLectureInfo;
	}
	
	ActiveLectureInfo getActiveLectureInfo() {
		return activeLectureInfo;
	}
	
	public LectureSession getLectureSession() {
		if (lectureSession == null) {
			lectureSession = dao.getEntity(LectureSession.class, activeLectureInfo.getLectureSessionId());
		}
		return lectureSession;
	}
	
	public int getLectureSessionId() {
		return activeLectureInfo.getLectureSessionId();
	}
	
	public Course getCourse() {
		if (course == null) {
			course = dao.getEntity(Course.class, activeLectureInfo.getCourseId());
		}
		return course;
	}
	
	public int getCourseId() {
		return activeLectureInfo.getCourseId();
	}
	
	public Lecture getLecture() {
		if (lecture == null) {
			lecture = dao.getEntity(Lecture.class, activeLectureInfo.getLectureId());
		}
		return lecture;
	}
	
	public int getLectureId() {
		return activeLectureInfo.getLectureId();
	}
	
	public Integer getCurrentSlideId() {
		return activeLectureInfo.getCurrentSlideId();
	}
	
	/** Returns the current SlideSession.
	 */
	public SlideSession getCurrentSlideSession() {
		if (activeLectureInfo.getCurrentSlideSessionId() != null) {
			return dao.getEntity(SlideSession.class, activeLectureInfo.getCurrentSlideSessionId());
		}
		else {
			return null;
		}
	}
	
	public Integer getCurrentSlideSessionId() {
		return activeLectureInfo.getCurrentSlideSessionId();
	}
	
	/** Sets the current slide for an active lecture.
	 * @param index the index of the slide (0-based)
	 */
	public void setCurrentSlideIndex(int index) {
		Slide slide = getLectureSession().getLecture().getSlides().get(index);
		
		SlideSession currentSlideSession = lectureSession.getSlideSessions().get(slide);
		
		if (currentSlideSession == null) {
			currentSlideSession = new SlideSession();
			currentSlideSession.setLectureSession(lectureSession);
			currentSlideSession.setSlide(slide);
			dao.save(currentSlideSession);
		}
		
		activeLectureInfo.setCurrentSlideSession(currentSlideSession);
	}
	
	public Whiteboard getCurrentWhiteboard() {
		if (activeLectureInfo.getCurrentWhiteboardId() != null) {
			return dao.getEntity(Whiteboard.class, activeLectureInfo.getCurrentWhiteboardId());
		}
		else {
			return null;
		}
	}
	
	public Integer getCurrentWhiteboardId() {
		return activeLectureInfo.getCurrentWhiteboardId();
	}
	
	public void setCurrentWhiteboardIndex(int index) {
		Whiteboard whiteboard = null;

		if (getLectureSession().getWhiteboards().size() > index) {
			whiteboard = lectureSession.getWhiteboards().get(index);
		}
		else {
			/* TODO we probably don't want to fill the lecture session with empty witeboards,
			 * but they are needed if a UP presentation is synched and it has more whiteboards 
			 * than the session
			 */
			while (lectureSession.getWhiteboards().size() <= index) {
				whiteboard = new Whiteboard(lectureSession.getWhiteboards().size());
				whiteboard.setLectureSession(lectureSession);
				lectureSession.getWhiteboards().add(whiteboard);
				dao.save(whiteboard);
			}
		}

		activeLectureInfo.setCurrentWhiteboard(whiteboard);
	}
	
	/** Returns the ink count of the current slide session.
	 * @throws NullPointerException in there is no current slide session
	 */
	public Integer getCurrentSlideSessionInkCount() {
		return activeLectureInfo.getCurrentSlideSessionInkCount();
	}
	
	public int slideInkAdded() {
		SlideSession slideSession = getCurrentSlideSession();
		int result = slideSession.incrementInkCount();
		
		dao.flush();
		activeLectureInfo.setCurrentSlideSessionInkCount(result);
		return result;
	}
	
	public Integer getCurrentWhiteboardInkCount() {
		return activeLectureInfo.getCurrentWhiteboardInkCount();
	}
	
	public int whiteboardInkAdded() {
		Whiteboard whiteboard = getCurrentWhiteboard();
		int result = whiteboard.incrementInkCount();
		
		dao.flush();
		activeLectureInfo.setCurrentWhiteboardInkCount(result);
		return result;
	}
	
	public int getStateCount() {
		return activeLectureInfo.getStateCount();
	}
	
	/** Blocks until the lecture state changes changes.
	 * @param currentSlideSessionId
	 * @return
	 * @throws InterruptedException if interrupted while waiting
	 */
	public int waitForStateChange(int previousStateCount) throws InterruptedException {
		synchronized (activeLectureInfo) {
			if (activeLectureInfo.getStateCount() == previousStateCount) {
				activeLectureInfo.wait();
			}
			
			return activeLectureInfo.getStateCount();
		}
	}
}
