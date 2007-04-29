/* $Id$ */

package org.p2presenter.pseudo;

import java.util.HashSet;
import java.util.Set;

public class ChildSpecification {
	private String defaultText;
	
	private HashSet<NodeDefinition> allowedNodeDefinitions = new HashSet<NodeDefinition>();
	private boolean list;
	private boolean optional;
	
	private Class requiredReturnType;
	
	/** Returns the default text.
	 */
	public String getDefaultText() {
		return defaultText;
	}
	
	/** Sets the default text.
	 * This text is shown when the child has not been added to the node.
	 */
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}
	
	public ChildSpecification(boolean list) {
		this.list = list;
	}
	
	public ChildSpecification(Class requiredReturnType) {
		this.requiredReturnType = requiredReturnType;
	}
	
	public Set<NodeDefinition> getAllowedNodeDefinitions() {
		return allowedNodeDefinitions;
	}
	
	public void setAllowedNodeDefinitions(HashSet<NodeDefinition> allowedNodeDefinitions) {
		this.allowedNodeDefinitions = allowedNodeDefinitions;
	}

	public boolean isList() {
		return list;
	}
	
	public void setList(boolean list) {
		this.list = list;
	}
	
	public boolean isOptional() {
		return optional;
	}
	
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public Class getRequiredReturnType() {
		return requiredReturnType;
	}
	
	public void setRequiredReturnType(Class requiredReturnType) {
		this.requiredReturnType = requiredReturnType;
	}

}