package org.p2presenter.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SubmissionDefinition<T extends Submission> extends AbstractSimpleEntity {
	private Slide slide;
	private int index;
	private String title;

	public SubmissionDefinition() {}

	public SubmissionDefinition(Slide slide) {
		this.slide = slide;
		this.index = slide.getSubmissionDefinitions().size();
		slide.getSubmissionDefinitions().add(this);
	}

	@ManyToOne
	public Slide getSlide() {
		return slide;
	}

	public void setSlide(Slide slide) {
		this.slide = slide;
	}

	@Column(name = "idx")
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
