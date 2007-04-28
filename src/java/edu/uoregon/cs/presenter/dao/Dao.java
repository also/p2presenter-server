/* $Id:Dao.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.dao;

import java.io.Serializable;
import java.util.List;

import org.p2presenter.server.model.Course;
import org.p2presenter.server.model.Lecture;


public interface Dao {
	
	public Lecture getLectureByTitle(Course course, String title);
	
	/** Returns the entity of the given <code>Class</code> with the given numeric identifier.
	 * @return the entity, or <code>null</code> if none match
	 */
	public <T> T getEntity(Class<T> entityClass, Serializable id);
	
	public <T> T loadEntity(Class<T> entityClass, Serializable id);
	
	public void makePersistent(Object entity);
	
	public void flush();
	
	/** Saves an entity, returning it's identifier.
	 * @param o the entity to save
	 * @return the identifier of the entity
	 */
	public Serializable save(Object o);
	
	/** Returns a <code>List</code> of all subjects.
	 */
	public List<String> getSubjects();
	
	/** Returns a <code>List</code> of all <code>Course</code>s in a subject.
	 * @param subject
	 * @return
	 */
	public List<Course> getCoursesInSubject(String subject);

}