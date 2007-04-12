/* $Id$ */

package edu.uoregon.cs.presenter.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.NotNull;

/** An interactivity, presented in a particular session.
 * @author Ryan Berdeen
 *
 */
public class InteractivitySession {
	private Integer id;
	
	private InteractivityDefinition interactivityDefinition;
	
	private SlideSession slideSession;
	
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
	public InteractivityDefinition getInteractivityDefinition() {
		return interactivityDefinition;
	}
	
	public void setInteractivityDefinition(InteractivityDefinition interactivityDefinition) {
		this.interactivityDefinition = interactivityDefinition;
	}
	
	@ManyToOne
	public SlideSession getSlideSession() {
		return slideSession;
	}
	
	public void setSlideSession(SlideSession slideSession) {
		this.slideSession = slideSession;
	}
}
