/* $Id$ */

package edu.uoregon.cs.presenter.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class InteractivityDefinition {
	private int id;
	private String clientViewClassName;
	private String clientModelClassName;
	
	public InteractivityDefinition() {}
	
	public InteractivityDefinition(int id, String clientViewClassName, String modelClassName) {
		this.id = id;
		this.clientViewClassName = clientViewClassName;
		this.clientModelClassName = modelClassName;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getClientViewClassName() {
		return clientViewClassName;
	}
	
	public void setClientViewClassName(String clientViewClassName) {
		this.clientViewClassName = clientViewClassName;
	}

	public String getClientModelClassName() {
		return clientModelClassName;
	}
	
	public void setClientModelClassName(String clientModelClassName) {
		this.clientModelClassName = clientModelClassName;
	}
}
