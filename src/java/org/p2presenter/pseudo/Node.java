/* $Id: AbstractNode.java 156 2007-04-14 02:53:28Z rberdeen $ */

package org.p2presenter.pseudo;

import java.util.HashMap;

public abstract class Node<T> {
	private Node<?>[] children;
	
	private static final HashMap<Class, Class> PRIMITIVE_TYPE_MAP = new HashMap<Class, Class>();
	static {
		PRIMITIVE_TYPE_MAP.put(Byte.TYPE, Byte.class);
		PRIMITIVE_TYPE_MAP.put(Short.TYPE, Short.class);
		PRIMITIVE_TYPE_MAP.put(Integer.TYPE, Integer.class);
		PRIMITIVE_TYPE_MAP.put(Long.TYPE, Long.class);
		PRIMITIVE_TYPE_MAP.put(Float.TYPE, Float.class);
		PRIMITIVE_TYPE_MAP.put(Double.TYPE, Double.class);
		PRIMITIVE_TYPE_MAP.put(Boolean.TYPE, Boolean.class);
		PRIMITIVE_TYPE_MAP.put(Character.TYPE, Character.class);
	}
	
	private ThreadLocal<Context> contextHolder = new ThreadLocal<Context>() {
		@Override
		protected Context initialValue() {
			return new Context();
		}
	};
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	
	public abstract T evaluate();
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	private Context getContext() {
		return contextHolder.get();
	}
	
	protected Scope getScope() {
		return getContext().scopes.peek();
	}
	
	protected <R> R evaluateChild(int index) {
		Node<R> child = getChild(index);
		R returnValue = child.evaluate();
		
		return returnValue;
	}
	
	@SuppressWarnings("unchecked")
	protected <C extends Node> C getChild(int index) {
		return (C) children[index];
	}
	
	public int getChildCount() {
		return children != null ? children.length : 0;
	}
	
	public abstract Class<? extends T> getReturnType();
}
