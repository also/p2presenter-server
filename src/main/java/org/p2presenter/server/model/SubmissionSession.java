package org.p2presenter.server.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

@Entity
public class SubmissionSession<T extends Submission> extends AbstractSimpleEntity {
	private SubmissionDefinition<T> submissionDefinition;
	private SlideSession slideSession;
	private Set<T> submissions;
	private Date began;
	private Date ended;

	public SubmissionSession() {}

	public SubmissionSession(SubmissionDefinition<T> submissionDefinition, SlideSession slideSession) {
		this.submissionDefinition = submissionDefinition;
		this.slideSession = slideSession;
	}

	@Temporal(TemporalType.TIME)
	public Date getBegan() {
		return began;
	}
	public void setBegan(Date began) {
		this.began = began;
	}

	@Temporal(TemporalType.TIME)
	public Date getEnded() {
		return ended;
	}
	public void setEnded(Date ended) {
		this.ended = ended;
	}

	@ManyToOne
	@NotNull
	public SlideSession getSlideSession() {
		return slideSession;
	}
	public void setSlideSession(SlideSession slideSession) {
		this.slideSession = slideSession;
	}

	@ManyToOne(targetEntity = SubmissionDefinition.class)
	@NotNull
	public SubmissionDefinition<T> getSubmissionDefinition() {
		return submissionDefinition;
	}
	public void setSubmissionDefinition(SubmissionDefinition<T> submissionDefinition) {
		this.submissionDefinition = submissionDefinition;
	}

	@OneToMany(targetEntity = Submission.class, mappedBy = "submissionSession")
	@OrderBy("timestamp")
	public Set<T> getSubmissions() {
		return submissions;
	}
	public void setSubmissions(Set<T> submissions) {
		this.submissions = submissions;
	}


}
