/* $Id$ */

package edu.uoregon.cs.presenter.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class InteractivityDefinition {
	private int id;
	private String hostControllerClassName;
	private String participantViewClassName;
	private String participantModelClassName;
	
	public InteractivityDefinition() {}
	
	public InteractivityDefinition(int id, String hostControllerClassName, String participantViewClassName, String participantModelClassName) {
		this.id = id;
		this.hostControllerClassName = hostControllerClassName;
		this.participantViewClassName = participantViewClassName;
		this.participantModelClassName = participantModelClassName;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getHostControllerClassName() {
		return hostControllerClassName;
	}

	public void setHostControllerClassName(String hostControllerClassName) {
		this.hostControllerClassName = hostControllerClassName;
	}

	public String getParticipantModelClassName() {
		return participantModelClassName;
	}

	public void setParticipantModelClassName(String participantModelClassName) {
		this.participantModelClassName = participantModelClassName;
	}

	public String getParticipantViewClassName() {
		return participantViewClassName;
	}

	public void setParticipantViewClassName(String participantViewClassName) {
		this.participantViewClassName = participantViewClassName;
	}
}
