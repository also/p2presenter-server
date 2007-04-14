/* $Id$ */

package edu.uoregon.cs.presenter.pseudo;

public class SingleChildSpecification implements ChildSpecification {
	private Class requiredReturnType;
	
	public Class getChildClass() {
		return null;
	}

	public int getMax() {
		return 1;
	}

	public int getMin() {
		return 1;
	}

	public Class getRequiredReturnType() {
		return requiredReturnType;
	}

}
