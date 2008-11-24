package org.p2presenter.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.ry1.springframework.web.util.Bindable;

/** A collection of slides.
 * A lecture may be a part of multiple courses, and may be presented multiple times in each.
 * @author rberdeen
 *
 */
@Entity
@Table
public class Lecture extends AbstractSimpleEntity {
	private Person creator;
	private String title;

	private Course course;
	private List<Slide> slides = new ArrayList<Slide>();

	private Set<LectureSession> lectureSessions;

	@ManyToOne
	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	/** Returns the Courses this Lecture is a part of.
	 */
	@ManyToOne
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
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

	@Bindable
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return getTitle();
	}
}
