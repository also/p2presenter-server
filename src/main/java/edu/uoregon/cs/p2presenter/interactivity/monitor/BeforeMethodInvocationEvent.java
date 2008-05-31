/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.lang.reflect.Method;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityModel;

/** Stores information about a method call for replay.
 * @author rberdeen
 *
 */
public class BeforeMethodInvocationEvent<T extends InteractivityModel> implements InteractivityEvent<T> {
	private SerializedStateEvent<T> currentStateEvent;
	
	private AfterMethodInvocationEvent afterMethodInvocationEvent;
	
	// FIXME shouldn't just be storing these
	private Object target;
	private Method method;
	private Object[] args;
	
	public BeforeMethodInvocationEvent(SerializedStateEvent<T> currentStateEvent, Object target, Method method, Object[] args) {
		this.currentStateEvent = currentStateEvent;
		this.target = target;
		this.method = method;
		this.args = args;
	}
	
	public void setAfterMethodInvocationEvent(AfterMethodInvocationEvent afterMethodInvocationEvent) {
		this.afterMethodInvocationEvent = afterMethodInvocationEvent;
	}
	
	public String toStringPart() {
		return method.toString();
	}
	
	public String toString() {
		return "Before " + toStringPart();
	}
	
	public SerializedStateEvent<T> getCurrentStateEvent() {
		return currentStateEvent;
	}

}
