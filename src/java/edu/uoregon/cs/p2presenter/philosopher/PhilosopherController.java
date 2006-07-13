/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

public interface PhilosopherController extends Philosopher {
	public HandState takeLeftChopstick();
	public HandState takeRightChopstick();
	
	public void releaseLeftChopstick();
	public void releaseRightChopstick();
}
