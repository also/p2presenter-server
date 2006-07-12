/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

public interface PhilosopherController extends Philosopher {
	public State takeLeftChopstick();
	public State takeRightChopstick();
	
	public void releaseLeftChopstick();
	public void releaseRightChopstick();
}
