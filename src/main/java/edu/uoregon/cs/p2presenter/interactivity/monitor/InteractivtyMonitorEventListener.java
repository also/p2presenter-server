/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

/** Listener interface for interactivity events.
 * @author Ryan Berdeen
 *
 */
public interface InteractivtyMonitorEventListener {
	/** Called after the event has been added to the monitor.
	 * @param event the event that was added.
	 */
	public void onInteractivityEvent(InteractivityEvent event);
}
