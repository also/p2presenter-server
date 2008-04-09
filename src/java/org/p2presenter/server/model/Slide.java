/* $Id:Slide.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package org.p2presenter.server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.NotNull;

@Entity
@Table
public class Slide extends AbstractSimpleEntity {
	private int index;
	
	private Lecture lecture;
	
	private List<SubmissionDefinition> submissionDefinitions = new ArrayList<SubmissionDefinition>();
	
	private InteractivityDefinition interactivityDefinition; 
	
	private Set<SlideSession> slideSessions = new HashSet<SlideSession>();
	
	private String title;
	private String body;

	public Slide() {}
	
	public Slide(int index) {
		this.index = index;
	}
	
	@Column(name = "idx")
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	/** Returns the text making up the body of the <code>Slide</code>.
	 * Can be <code>null</code>.
	 */
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	/** Returns the <code>Lecture</code> that contains the <code>Slide</code>.
	 * Cannot be <code>null</code>.
	 */
	@NotNull
	@ManyToOne
	public Lecture getLecture() {
		return lecture;
	}

	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}
	
	/** Returns the <code>SlideSession</code>s in which this slide was presented.
	 * Cannot be <code>null</code>.
	 */
	@OneToMany(mappedBy = "slide")
	public Set<SlideSession> getSlideSessions() {
		return slideSessions;
	}
	
	public void setSlideSessions(Set<SlideSession> slideSessions) {
		this.slideSessions = slideSessions;
	}

	@OneToMany(mappedBy = "slide")
	@IndexColumn(name = "idx")
	public List<SubmissionDefinition> getSubmissionDefinitions() {
		return submissionDefinitions;
	}

	public void setSubmissionDefinitions(List<SubmissionDefinition> submissionDefinitions) {
		this.submissionDefinitions = submissionDefinitions;
	}

	/** Returns the title of the slide. Can be <code>null</code>.
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ManyToOne
	public InteractivityDefinition getInteractivityDefinition() {
		return interactivityDefinition;
	}
	
	public void setInteractivityDefinition(
			InteractivityDefinition interactivityDefinition) {
		this.interactivityDefinition = interactivityDefinition;
	}
}
