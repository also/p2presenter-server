/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

/** Stores information about an interactivity event for replay.
 * @author rberdeen
 *
 */
public interface InteractivityEvent {
	public void replay(InteractivityReplay interactivityReplay);
}
