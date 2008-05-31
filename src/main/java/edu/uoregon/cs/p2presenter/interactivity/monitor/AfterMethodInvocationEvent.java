/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityModel;

/** Stores information about the return value of a method for replay.
 * @author rberdeen
 *
 */
public class AfterMethodInvocationEvent<T extends InteractivityModel> implements InteractivityEvent<T> {
	private SerializedStateEvent<T> currentStateEvent;
	
	private BeforeMethodInvocationEvent beforeMethodInvocationEvent;
	
	public AfterMethodInvocationEvent(SerializedStateEvent<T> currentStateEvent, BeforeMethodInvocationEvent<T> beforeMethodInvocationEvent, Object result) {
		this.currentStateEvent = currentStateEvent;
		this.beforeMethodInvocationEvent = beforeMethodInvocationEvent;
		beforeMethodInvocationEvent.setAfterMethodInvocationEvent(this);
	}
	
	@Override
	public String toString() {
		return "After " + beforeMethodInvocationEvent.toStringPart();
	}
	
	public SerializedStateEvent<T> getCurrentStateEvent() {
		return currentStateEvent;
	}

}
