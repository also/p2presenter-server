/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

public class InteractivityDefinition {
	private int id;
	private String clientViewClassName;
	private String clientModelClassName;
	
	public InteractivityDefinition(int id, String clientViewClassName, String modelClassName) {
		this.id = id;
		this.clientViewClassName = clientViewClassName;
		this.clientModelClassName = modelClassName;
	}

	public int getId() {
		return id;
	}
	
	public String getClientViewClassName() {
		return clientViewClassName;
	}

	public String getModelClassName() {
		return clientModelClassName;
	}
}
