/* $Id:Person.java 62 2007-01-08 04:14:12Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.presenter.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.Length;

/**
 * A person may both teach and attend courses.
 * @author rberdeen
 *
 */
@Entity
@Table
public class Person {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	
	private Set<Course> coursesTaught = new HashSet<Course>();
	private Set<Course> coursesAttended = new HashSet<Course>();
	private Set<String> roles = new HashSet<String>();
	
	/** Returns the username.
	 */
	@Id
	@Length(min = 3, max = 20)
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Length(min = 6)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	/** Returns the Courses this Person attends.
	 */
	@ManyToMany
	@OrderBy("subject, number, crn")
	public Set<Course> getCoursesAttended() {
		return coursesAttended;
	}
	
	public void setCoursesAttended(Set<Course> coursesAttended) {
		this.coursesAttended = coursesAttended;
	}
	
	/** Returns the courses this person teaches.
	 */
	@OneToMany(mappedBy = "instructor")
	@OrderBy("subject, number, crn")
	public Set<Course> getCoursesTaught() {
		return coursesTaught;
	}
	
	public void setCoursesTaught(Set<Course> courses) {
		this.coursesTaught = courses;
	}
	
	/** Returns the first name.
	 */
	@Length(max = 30)
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/** Returns the last name.
	 */
	@Length(max = 30)
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@CollectionOfElements
	public Set<String> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString() {
		return firstName == null || lastName == null ? username : firstName + " " + lastName;
	}
	
}
