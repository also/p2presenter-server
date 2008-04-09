/* $Id$ */

package org.p2presenter.server.model;

import javax.persistence.Entity;

import org.hibernate.validator.NotNull;

@Entity
public class InteractivityDefinition extends AbstractSimpleEntity {
	private String hostControllerClassName;
	private String participantViewClassName;
	private String participantModelInterfaceClassName;
	
	public InteractivityDefinition() {}
	
	public InteractivityDefinition(String hostControllerClassName, String participantViewClassName, String participantModelInterfaceClassName) {
		this.hostControllerClassName = hostControllerClassName;
		this.participantViewClassName = participantViewClassName;
		this.participantModelInterfaceClassName = participantModelInterfaceClassName;
	}

	@NotNull
	public String getHostControllerClassName() {
		return hostControllerClassName;
	}

	public void setHostControllerClassName(String hostControllerClassName) {
		this.hostControllerClassName = hostControllerClassName;
	}

	@NotNull
	public String getParticipantModelInterfaceClassName() {
		return participantModelInterfaceClassName;
	}

	public void setParticipantModelInterfaceClassName(String participantModelClassName) {
		this.participantModelInterfaceClassName = participantModelClassName;
	}

	@NotNull
	public String getParticipantViewClassName() {
		return participantViewClassName;
	}

	public void setParticipantViewClassName(String participantViewClassName) {
		this.participantViewClassName = participantViewClassName;
	}
}
