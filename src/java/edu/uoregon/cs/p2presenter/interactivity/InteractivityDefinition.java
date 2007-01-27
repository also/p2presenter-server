/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

public class InteractivityDefinition {
	private int id;
	private Class modelClass;
	
	public InteractivityDefinition(int id, Class modelClass) {
		this.id = id;
		this.modelClass = modelClass;
	}

	public int getId() {
		return id;
	}

	public Class getModelClass() {
		return modelClass;
	}
}
