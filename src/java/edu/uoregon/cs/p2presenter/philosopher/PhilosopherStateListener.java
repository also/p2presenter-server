/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import edu.uoregon.cs.p2presenter.remoting.Asynchronous;

public interface PhilosopherStateListener {
	@Asynchronous
	public void philosopherStateChanged(Philosopher philosopher);
}
