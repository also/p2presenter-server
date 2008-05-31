/* $Id$ */

package org.p2presenter.pseudo;

public class LiteralNode<T extends Object> extends Node<T> {
	private T value;
	
	LiteralNode(T value) {
		this.value = value;
	}

	@Override
	public T evaluate() {
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getReturnType() {
		return (Class<T>) value.getClass();
	}

}
