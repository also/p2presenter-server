/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import org.p2presenter.remoting.Asynchronous;

public interface PhilosopherStateListener {
	@Asynchronous
	public void philosopherStateChanged(Philosopher philosopher, Philosopher.State philosopherState, Philosopher.Hand.State leftHandState, Philosopher.Hand.State rightHandState);
}
