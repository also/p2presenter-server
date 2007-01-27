/* $Id:Submission.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Submission<T extends SubmissionDefinition> {
	private Integer id;
	private SubmissionSession<?> submissionSession;
	private SlideSession slideSession;
	private Person student;
	private Date timestamp;
	
	public Submission() {
		timestamp = new Date();
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	/** Returns the student that submitted 
	 */
	@ManyToOne
	@NotNull
	public Person getStudent() {
		return student;
	}
	
	public void setStudent(Person student) {
		this.student = student;
	}

	@ManyToOne
	public SlideSession getSlideSession() {
		return slideSession;
	}

	public void setSlideSession(SlideSession slideSession) {
		this.slideSession = slideSession;
	}

	/** Returns the session the submission was submitted.
	 * Note that <code>null</code> sessions are allowed to enable students to send unsolicited submissions or save their work.
	 * These undefined submissions would primarily be useful for freeform submissions.
	 */
	@ManyToOne
	public SubmissionSession<?> getSubmissionSession() {
		return submissionSession;
	}
	
	public void setSubmissionSession(SubmissionSession<?> submissionSession) {
		this.submissionSession = submissionSession;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}
