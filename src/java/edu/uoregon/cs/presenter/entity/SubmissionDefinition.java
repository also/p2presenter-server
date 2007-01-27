/* $Id:SubmissionDefinition.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class SubmissionDefinition<T extends Submission> {
	private Integer id;
	private Slide slide;
	private int index;
	private String title;
	
	public SubmissionDefinition() {}
	
	public SubmissionDefinition(Slide slide) {
		this.slide = slide;
		this.index = slide.getSubmissionDefinitions().size();
		slide.getSubmissionDefinitions().add(this);
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
