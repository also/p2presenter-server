/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher.host;

import edu.uoregon.cs.p2presenter.philosopher.Philosopher;

public interface PhilosopherStateListener {
	public void philosopherStateChanged(Philosopher philosopher);
}
