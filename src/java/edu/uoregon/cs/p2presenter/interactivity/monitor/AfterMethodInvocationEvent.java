/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

/** Stores information about the return value of a method for replay.
 * @author rberdeen
 *
 */
public class AfterMethodInvocationEvent implements InteractivityEvent {
	private BeforeMethodInvocationEvent beforeMethodInvocationEvent;
	
	public AfterMethodInvocationEvent(BeforeMethodInvocationEvent beforeMethodInvocationEvent, Object result) {
		this.beforeMethodInvocationEvent = beforeMethodInvocationEvent;
		beforeMethodInvocationEvent.setAfterMethodInvocationEvent(this);
	}

	public void replay(InteractivityReplay interactivityReplay) {
		// TODO Auto-generated method stub
		
	}

}
