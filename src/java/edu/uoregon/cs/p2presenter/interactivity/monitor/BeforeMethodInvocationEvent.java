/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.lang.reflect.Method;

/** Stores information about a method call for replay.
 * @author rberdeen
 *
 */
public class BeforeMethodInvocationEvent implements InteractivityEvent {
	private AfterMethodInvocationEvent afterMethodInvocationEvent;
	
	public BeforeMethodInvocationEvent(Object target, Method method, Object[] args) {
		// TODO not very useful
	}

	public void replay(InteractivityReplay interactivityReplay) {
		// TODO Auto-generated method stub
		
	}
	
	public void setAfterMethodInvocationEvent(AfterMethodInvocationEvent afterMethodInvocationEvent) {
		this.afterMethodInvocationEvent = afterMethodInvocationEvent;
	}

}
