/* $Id:ActiveLectureController.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.uoregon.cs.presenter.PresenterException;
import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Lecture;
import edu.uoregon.cs.presenter.entity.LectureSession;
import edu.uoregon.cs.presenter.entity.SubmissionSession;

public class ActiveLectureController {
	private Dao dao;
	
	private HashMap<Integer, ActiveLectureInfo> activeLecturesInfo = new HashMap<Integer, ActiveLectureInfo>();
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public ActiveLecture getActiveLecture(int lectureSessionId) {
		ActiveLectureInfo activeLectureInfo = activeLecturesInfo.get(lectureSessionId);
		
		return activeLectureInfo != null ? new ActiveLecture(dao, activeLectureInfo) : null;
	}
	
	public ActiveLecture getActiveLecture(LectureSession lectureSession) {
		return getActiveLecture(lectureSession.getId());
	}
	
	/** Returns the active ActiveLectures for all Lectures in a Course.
	 */
	public Set<ActiveLecture> getActiveLectures(Course course) {
		LinkedHashSet<ActiveLecture> result = new LinkedHashSet<ActiveLecture>();
		
		for (ActiveLectureInfo activeLectureInfo : activeLecturesInfo.values()) {
			if (activeLectureInfo.getCourseId() == course.getCrn()) {
				result.add(new ActiveLecture(dao, activeLectureInfo));
			}
		}
		
		return result;
	}
	
	public ActiveLecture getActiveLecture(Course course, Lecture lecture) {
		return getActiveLecture(course.getCrn(), lecture.getId());
	}
	
	public ActiveLecture getActiveLecture(int courseId, int lectureId) {
		for (ActiveLectureInfo activeLectureInfo : activeLecturesInfo.values()) {
			if (activeLectureInfo.getCourseId() == courseId && activeLectureInfo.getLectureId() == lectureId) {
				return new ActiveLecture(dao, activeLectureInfo);
			}
		}
		
		return null;
	}
	
	/** Creates and activates a LectureSession for a Lecture in a Course.
	 * @param course the course in which the Lecture is being presented
	 * @param lecture the lecture being presented
	 * @return the LectureSession created
	 */
	public LectureSession newLectureSession(Course course, Lecture lecture) {
		if (getActiveLecture(course, lecture) != null) {
			throw new PresenterException("Lecture already active for course");
		}
		
		LectureSession lectureSession = new LectureSession(course, lecture);
		
		dao.save(lectureSession);
		lecture.getLectureSessions().add(lectureSession);
		
		reactivateLectureSession(lectureSession);
		
		return lectureSession;
	}
	
	/** Begins tracking the LectureSession.
	 * @param lectureSession
	 */
	public void reactivateLectureSession(LectureSession lectureSession) {
		ActiveLectureInfo activeLecture = new ActiveLectureInfo(lectureSession.getId(), lectureSession.getCourse().getCrn(), lectureSession.getLecture().getId());
		activeLecturesInfo.put(lectureSession.getId(), activeLecture);
	}
	
	/**
	 * @param lectureSession
	 */
	public void endActiveLecture(ActiveLecture activeLecture) {
		ActiveLectureInfo activeLectureInfo = activeLecture.getActiveLectureInfo();
		activeLecturesInfo.remove(activeLectureInfo.getLectureSessionId());
		activeLectureInfo.stateChanged();
	}
	
	public void beginSubmissionSession(SubmissionSession<?> submissionSession) {
		// TODO track
		submissionSession.setBegin(new Date());
		dao.flush();
	}
	
	public void endSubmissionSession(SubmissionSession<?> submissionSession) {
		// TODO track
		submissionSession.setEnd(new Date());
		dao.flush();
	}
}
