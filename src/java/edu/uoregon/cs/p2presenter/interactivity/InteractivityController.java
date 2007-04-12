/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import java.awt.Container;

/** The controller for an interactivity.
 * @author Ryan Berdeen
 *
 * @param <T> the type of the participant models
 */
public interface InteractivityController<T> {
	public T onConnect();
	
	public void onDisconnect(T model);
	
	public Container getView();
}
