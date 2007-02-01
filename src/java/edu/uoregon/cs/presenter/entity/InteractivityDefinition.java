/* $Id$ */

package edu.uoregon.cs.presenter.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.NotNull;

@Entity
public class InteractivityDefinition {
	private int id;
	private String hostControllerClassName;
	private String participantViewClassName;
	private String participantModelInterfaceClassName;
	
	public InteractivityDefinition() {}
	
	public InteractivityDefinition(int id, String hostControllerClassName, String participantViewClassName, String participantModelInterfaceClassName) {
		this.id = id;
		this.hostControllerClassName = hostControllerClassName;
		this.participantViewClassName = participantViewClassName;
		this.participantModelInterfaceClassName = participantModelInterfaceClassName;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
