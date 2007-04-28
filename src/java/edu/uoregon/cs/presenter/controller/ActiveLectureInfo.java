/* $Id:ActiveLecture.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.controller;

import org.p2presenter.server.model.SlideSession;
import org.p2presenter.server.model.Whiteboard;


/** Stores information about an active LectureSession.
 * @author rberdeen
 *
 */
class ActiveLectureInfo {
	private int lectureSessionId;
	private int lectureId;
	private int courseId;
	
	private Integer currentSlideSessionId;
	private Integer currentSlideSessionInkCount;
	private Integer currentSlideId;
	
	private Integer currentWhiteboardId;
	private Integer currentWhiteboardInkCount;
	
	private int stateCount = 0;

	public ActiveLectureInfo(int lectureSessionId, int courseId, int lectureId) {
		this.lectureSessionId = lectureSessionId;
		this.courseId = courseId;
		this.lectureId = lectureId;
	}
	
	/** Returns the ID of the LectureSession.
	 */
	public int getLectureSessionId() {
		return lectureSessionId;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public int getLectureId() {
		return lectureId;
	}
	
	/** Returns the ID of the current slide session. Can be null.
	 */
	public Integer getCurrentSlideSessionId() {
		return currentSlideSessionId;
	}
	
	public Integer getCurrentSlideSessionInkCount() {
		return currentSlideSessionInkCount;
	}
	
	public void setCurrentSlideSessionInkCount(Integer currentSlideSessionInkCount) {
		this.currentSlideSessionInkCount = currentSlideSessionInkCount;
		stateChanged();
	}
	
	public Integer getCurrentSlideId() {
		return currentSlideId;
	}
	
	/** Sets the ID and ink count of the current slide session. Can be null.
	 */
	public void setCurrentSlideSession(SlideSession currentSlideSession) {
		if (currentSlideSession != null) {
			currentSlideSessionId = currentSlideSession.getId();
			currentSlideSessionInkCount = currentSlideSession.getInkCount();
			currentSlideId = currentSlideSession.getSlide().getId();
		}
		else {
			currentSlideSessionId = null;
			currentSlideSessionInkCount = null;
			currentSlideId = null;
		}
	
		currentWhiteboardId = null;
		currentWhiteboardInkCount = null;
		stateChanged();
	}

	public Integer getCurrentWhiteboardId() {
		return currentWhiteboardId;
	}
	
	public Integer getCurrentWhiteboardInkCount() {
		return currentWhiteboardInkCount;
	}
	
	public void setCurrentWhiteboardInkCount(Integer currentWhiteboardInkCount) {
		this.currentWhiteboardInkCount = currentWhiteboardInkCount;
		stateChanged();
	}

	public void setCurrentWhiteboard(Whiteboard currentWhiteboard) {
		if (currentWhiteboard != null) {
			currentWhiteboardId = currentWhiteboard.getId();
			currentWhiteboardInkCount = currentWhiteboard.getInkCount();
		}
		else {
			currentWhiteboardId = null;
			currentWhiteboardInkCount = null;
		}
		stateChanged();
	}
	
	public synchronized void stateChanged() {
		stateCount++;
		notifyAll();
	}
	
	public int getStateCount() {
		return stateCount;
	}
	
}
