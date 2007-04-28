/* $Id:SlideSession.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package org.p2presenter.server.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

/** A <code>Slide</code> presented at a particular time.
 *
 */
@Entity
@Table
public class SlideSession {
	private Integer id;
	
	private LectureSession lectureSession;
	
	private Slide slide;
	
	private int inkCount;
	
	private Set<SubmissionSession<?>> submissionSessions = new LinkedHashSet<SubmissionSession<?>>();
	
	private Set<Submission<?>> submissions = new LinkedHashSet<Submission<?>>();

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public int getInkCount() {
		return inkCount;
	}
	
	public void setInkCount(int inkCount) {
		this.inkCount = inkCount;
	}
	
	public int incrementInkCount() {
		return ++inkCount;
	}

	/** Returns the <code>LectureSession</code> in which this <code>SlideSession</code> was presented.
	 * Cannot be <code>null</code>
	 */
	@ManyToOne
	@NotNull
	public LectureSession getLectureSession() {
		return lectureSession;
	}

	public void setLectureSession(LectureSession lectureSession) {
		this.lectureSession = lectureSession;
	}

	/** Returns the <code>Slide</code> that was presented.
	 * Cannot be <code>null</code>.
	 */
	@ManyToOne
	@NotNull
	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}
	
	@OneToMany(mappedBy = "slideSession")
	@OrderBy("timestamp")
	public Set<Submission<?>> getSubmissions() {
		return submissions;
	}
	
	public void setSubmissions(Set<Submission<?>> submissions) {
		this.submissions = submissions;
	}
	
	@OneToMany(mappedBy = "slideSession")
	@OrderBy("began")
	public Set<SubmissionSession<?>> getSubmissionSessions() {
		return submissionSessions;
	}
	
	public void setSubmissionSessions(
			Set<SubmissionSession<?>> submissionSessions) {
		this.submissionSessions = submissionSessions;
	}
}
