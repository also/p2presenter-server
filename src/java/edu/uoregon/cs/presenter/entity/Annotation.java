/* $Id$ */

package edu.uoregon.cs.presenter.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table
public class Annotation {
	public enum Visibility {PRIVATE, INSTRUCTOR, SECTION};
	
	private Integer id;
	
	private SlideSession slideSession;
	
	private Person creator;
	
	private Date timestamp;
	
	private Visibility visibility;
	
	private String text;
	
	@NotNull
	@ManyToOne
	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne
	@NotNull
	public SlideSession getSlideSession() {
		return slideSession;
	}

	public void setSlideSession(SlideSession slideSession) {
		this.slideSession = slideSession;
	}

	@Length(max=16383)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Temporal(TemporalType.TIME)
	@NotNull
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Enumerated(EnumType.ORDINAL)
	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	@Transient
	public String getFilename() {
		// TODO
		return null;
	}
}
