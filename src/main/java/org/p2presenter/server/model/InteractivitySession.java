package org.p2presenter.server.model;

import javax.persistence.ManyToOne;

import org.hibernate.validator.NotNull;

/** An interactivity, presented in a particular session.
 * @author Ryan Berdeen
 *
 */
public class InteractivitySession extends AbstractSimpleEntity  {
	private InteractivityDefinition interactivityDefinition;

	private SlideSession slideSession;

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
