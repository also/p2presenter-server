/* $Id:Submission.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package org.p2presenter.server.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Submission<T extends SubmissionDefinition> extends AbstractSimpleEntity {
	private SubmissionSession<?> submissionSession;
	private SlideSession slideSession;
	private Person student;
	private Date timestamp;
	
	public Submission() {
		timestamp = new Date();
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
