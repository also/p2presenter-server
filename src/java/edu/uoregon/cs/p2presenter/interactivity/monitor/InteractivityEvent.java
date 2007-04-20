/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.monitor;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityModel;

/** Stores information about an interactivity event for replay.
 * @author rberdeen
 *
 */
public interface InteractivityEvent<T extends InteractivityModel> {
	public SerializedStateEvent<T> getCurrentStateEvent();
}
