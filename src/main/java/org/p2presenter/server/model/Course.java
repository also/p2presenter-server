/* $Id:Course.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package org.p2presenter.server.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/** A particular course.
 * @author rberdeen
 *
 */
@Entity
@Table
public class Course extends AbstractSimpleEntity {
	
	private Integer crn;
	
	private String title;
	private String subject;
	private Integer number;
	
	private Person instructor;
	private Set<Person> students;
	
	private Set<Lecture> lectures = new LinkedHashSet<Lecture>();
	
	public Integer getCrn() {
		return crn;
	}
	
	public void setCrn(Integer crn) {
		this.crn = crn;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	public Person getInstructor() {
		return instructor;
	}
	
	public void setInstructor(Person instructor) {
		this.instructor = instructor;
	}
	
	@OneToMany(mappedBy = "course")
	@OrderBy("title") // TODO order by something else
	public Set<Lecture> getLectures() {
		return lectures;
	}
	
	public void setLectures(Set<Lecture> lectures) {
		this.lectures = lectures;
	}

	@Column(name = "nr")
	public Integer getNumber() {
		return number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	@ManyToMany(mappedBy = "coursesAttended")
	@OrderBy("lastName, firstName")
	public Set<Person> getStudents() {
		return students;
	}
	
	public void setStudents(Set<Person> students) {
		this.students = students;
	}
	
	@Length(min = 1, max = 5)
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@NotNull
	@Length(min = 3, max = 255)
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return getSubject() + " " + getNumber() + ": " + getTitle();
	}
	
}
