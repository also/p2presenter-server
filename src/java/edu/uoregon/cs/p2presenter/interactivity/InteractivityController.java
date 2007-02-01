/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

import java.awt.Container;

public interface InteractivityController<T> {
	public T onConnect();
	
	public void onDisconnect(T model);
	
	public Container getView();
}
