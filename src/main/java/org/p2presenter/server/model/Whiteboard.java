/* $Id:Whiteboard.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package org.p2presenter.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.validator.NotNull;

@Entity
public class Whiteboard extends AbstractSimpleEntity {
	private LectureSession lectureSession;
	private int index;
	
	private int inkCount;
	
	public Whiteboard() {}
	
	public Whiteboard(int index) {
		this.index = index;
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
