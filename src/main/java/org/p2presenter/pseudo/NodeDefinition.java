package org.p2presenter.pseudo;

import java.util.List;

public class NodeDefinition {
	private String template;
	private Class<? extends Node<?>> nodeClass;
	private List<ChildSpecification> children;

	public NodeDefinition(String template) {
		super();
		this.template = template;
	}

	public final String getTemplate() {
		return template;
	}

	public final void setTemplate(String template) {
		this.template = template;
	}

	public final Class<? extends Node<?>> getNodeClass() {
		return nodeClass;
	}

	public final void setNodeClass(Class<? extends Node<?>> nodeClass) {
		this.nodeClass = nodeClass;
	}

	public final List<ChildSpecification> getChildren() {
		return children;
	}

	public final void setChildren(List<ChildSpecification> children) {
		this.children = children;
	}
}
