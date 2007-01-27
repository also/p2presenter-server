/* $Id:Whiteboard.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.NotNull;

@Entity
public class Whiteboard {
	private Integer id;
	private LectureSession lectureSession;
	private int index;
	
	private int inkCount;
	
	public Whiteboard() {}
	
	public Whiteboard(int index) {
		this.index = index;
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
	@NotNull
	public LectureSession getLectureSession() {
		return lectureSession;
	}
	
	public void setLectureSession(LectureSession lectureSession) {
		this.lectureSession = lectureSession;
	}
	
	@Column(name = "idx")
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
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
}
