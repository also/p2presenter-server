/* $Id$ */

package edu.uoregon.cs.presenter.pseudo;

import java.util.HashMap;

public abstract class AbstractNode {
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	public abstract Object evaluate();
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
}
