/* $Id:LectureSession.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package org.p2presenter.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.NotNull;

/** A lecture given at a particular time.
 * @author rberdeen
 *
 */
@Entity
@Table
public class LectureSession {
	private Integer id;
	
	private Course course;
	private Lecture lecture;
	
	private Map<Slide, SlideSession> slideSessions = new HashMap<Slide, SlideSession>();
	
	private List<Whiteboard> whiteboards = new ArrayList<Whiteboard>();
	
	private Date timestamp;
	
	public LectureSession() {
		timestamp = new Date();
	}
	
	public LectureSession(Course course, Lecture lecture) {
		this();
		this.course = course;
		this.lecture = lecture;
	}

	/** Returns the SlideSessions associated with this LectureSession. Cannot be null.
	 */
	@OneToMany(mappedBy = "lectureSession")
	@MapKey(name = "slide")
	public Map<Slide, SlideSession> getSlideSessions() {
		return slideSessions;
	}

	public void setSlideSessions(Map<Slide, SlideSession> slideSessions) {
		this.slideSessions = slideSessions;
	}
	
	@OneToMany(mappedBy = "lectureSession")
	@IndexColumn(name="idx")
	public List<Whiteboard> getWhiteboards() {
		return whiteboards;
	}
	
	public void setWhiteboards(List<Whiteboard> whiteboards) {
		this.whiteboards = whiteboards;
	}

	/** Returns the course in which this lecture was given.
	 */
	@ManyToOne
	@NotNull
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	/** Returns the lecture given.
	 */
	@ManyToOne
	@NotNull
	public Lecture getLecture() {
		return lecture;
	}

	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}
	
	/** Returns the timestamp of the beginning of the lecture.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}