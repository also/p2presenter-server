/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity;

public interface InteractivityRunner<T> {
	public T onConnect();
	
	public void onDisconnect(T model);
}
