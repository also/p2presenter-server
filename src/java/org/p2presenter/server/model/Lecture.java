/* $Id:Lecture.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package org.p2presenter.server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/** A collection of slides.
 * A lecture may be a part of multiple courses, and may be presented multiple times in each.
 * @author rberdeen
 *
 */
@Entity
@Table
public class Lecture {
	private Integer id;
	private Person creator;
	private String title;

	private Set<Course> courses = new HashSet<Course>();
	private List<Slide> slides = new ArrayList<Slide>();
	
	private Set<LectureSession> lectureSessions;
	
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne
	public Person getCreator() {
		return creator;
	}
	
	public void setCreator(Person creator) {
		this.creator = creator;
	}
	
	/** Returns the Courses this Lecture is a part of.
	 */
	@ManyToMany
	@OrderBy("subject, number, crn")
	public Set<Course> getCourses() {
		return courses;
	}
	
	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
	
	/** Returns the LectureSessions in which this Lecture was presented.
	 */
	@OneToMany(mappedBy="lecture")
	@OrderBy("timestamp DESC")
	public Set<LectureSession> getLectureSessions() {
		return lectureSessions;
	}
	
	public void setLectureSessions(Set<LectureSession> lectureSessions) {
		this.lectureSessions = lectureSessions;
	}
	
	/** Returns the slides that make up this Lecture.
	 */
	@OneToMany(mappedBy = "lecture")
	@IndexColumn(name="idx")
	public List<Slide> getSlides() {
		return slides;
	}
	
	public void setSlides(List<Slide> slides) {
		this.slides = slides;
	}
	
	/** Returns the title of this Lecture.
	 */
	@NotNull
	@Length(min = 3, max = 255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}
}
