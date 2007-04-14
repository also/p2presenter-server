/* $Id$ */

package edu.uoregon.cs.presenter.pseudo;

public class ChildListSpecification implements ChildSpecification {
	private int min;
	private int max;
	
	private Class childClass;

	public final Class getChildClass() {
		return childClass;
	}

	public final int getMax() {
		return max;
	}

	public final int getMin() {
		return min;
	}

	public final Class getRequiredReturnType() {
		/* lists don't have a return type */
		return null;
	}
}
