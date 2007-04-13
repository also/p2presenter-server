/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import java.util.List;

/** Replays an interactivity.
 * @author rberdeen
 *
 */
public class InteractivityReplay {
	/**  */
	private List<InteractivityEvent> interactivityEvents;
	
	private int currentinteractivityEventIndex;
	
	private StateObjectInvocationHandler stateObjectInvocationHandler;
	
	public boolean hasNext() {
		return currentinteractivityEventIndex < interactivityEvents.size() - 1;
	}
	
	/** Advance to the next event.
	 * 
	 */
	public synchronized void next() {
		if (hasNext()) {
			currentinteractivityEventIndex++;
			replayCurrentEvent();
		}
		else {
			// TODO
			throw new IllegalStateException();
		}
	}
	
	public boolean hasPrevious() {
		return currentinteractivityEventIndex > 1;
	}
	
	/** Return to the previous event.
	 * 
	 */
	public synchronized void previous() {
		if (hasPrevious()) {
			currentinteractivityEventIndex--;
			replayCurrentEvent();
		}
		else {
			// TODO
			throw new IllegalStateException();
		}
	}
	
	/** replays the current event */
	private void replayCurrentEvent() {
		interactivityEvents.get(currentinteractivityEventIndex).replay(this);
	}
	
	/** Called by {@link SerializedStateEvent#replay(InteractivityReplay)}.
	 * @param currentState
	 */
	void setCurrentState(Object currentState) {
		stateObjectInvocationHandler.setCurrentState(currentState);
	}
}
