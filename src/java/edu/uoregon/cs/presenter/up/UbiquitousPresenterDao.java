/* $Id:UbiquitousPresenterDao.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.up;

import org.springframework.util.StringUtils;

import edu.uoregon.cs.presenter.controller.ActiveLecture;
import edu.uoregon.cs.presenter.controller.ActiveLectureController;
import edu.uoregon.cs.presenter.dao.Dao;
import edu.uoregon.cs.presenter.entity.Course;
import edu.uoregon.cs.presenter.entity.Lecture;

public class UbiquitousPresenterDao {
	private Dao dao;
	private ActiveLectureController activeLectureController;
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public void setActiveLectureController(ActiveLectureController activeLectureController) {
		this.activeLectureController = activeLectureController;
	}
	
	public Course getCourse(String string) {
		if (!StringUtils.hasText(string)) {
			throw new IllegalArgumentException("Course name may not be empty");
		}
		return getEntity(Course.class, string);
	}
	
	public Course loadCourse(String string) {
		if (!StringUtils.hasText(string)) {
			throw new IllegalArgumentException("Course name may not be empty");
		}
		return loadEntity(Course.class, string);
	}
	
	public Lecture getLecture(String string) {
		if (!StringUtils.hasText(string)) {
			throw new IllegalArgumentException("Lecture name may not be empty");
		}
		return getEntity(Lecture.class, string);
	}
	
	public Lecture loadLecture(String string) {
		if (!StringUtils.hasText(string)) {
			throw new IllegalArgumentException("Lecture name may not be empty");
		}
		return getEntity(Lecture.class, string);
	}
	
	private <T> T getEntity(Class<T> entityClass, String string) {
		return dao.getEntity(entityClass, getId(string));
	}
	
	private <T> T loadEntity(Class<T> entityClass, String string) {
		return dao.loadEntity(entityClass, getId(string));
	}
	
	private Integer getId(String string) {
		return new Integer(string.substring(string.lastIndexOf('(') + 1, string.length() - 1));
	}
	
	public ActiveLecture getActiveLecture(String classroomString, String lectureString) {
		return activeLectureController.getActiveLecture(getId(classroomString), getId(lectureString));
	}
}