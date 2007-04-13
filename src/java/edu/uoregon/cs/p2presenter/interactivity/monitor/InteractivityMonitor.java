/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.lang.reflect.Method;
import java.util.ArrayList;

import edu.uoregon.cs.p2presenter.remoting.InvocationListener;

/** Records events from interactivities.
 * @author rberdeen
 *
 */
public class InteractivityMonitor implements InvocationListener<BeforeMethodInvocationEvent>, StateListener {
	private ArrayList<InteractivityEvent> interactivityEvents = new ArrayList<InteractivityEvent>();
	
	/** The object storing the interactivities state */
	private Object state;
	
	public synchronized void stateChanged() {
		interactivityEvents.add(new SerializedStateEvent(state));
	}

	public void afterMethodInvocation(BeforeMethodInvocationEvent before, Object result) {
		interactivityEvents.add(new AfterMethodInvocationEvent(before, result));
	}

	public BeforeMethodInvocationEvent beforeMethodInvocation(Object target, Method method, Object[] args) {
		BeforeMethodInvocationEvent result = new BeforeMethodInvocationEvent(target, method, args);
		interactivityEvents.add(result);
		
		return result;
	}
}
