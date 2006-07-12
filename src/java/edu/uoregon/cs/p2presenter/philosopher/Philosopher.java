/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

public interface Philosopher {
	public enum State { EATING, WAITING, MEDITATING, INTERMEDIATE, INACTIVE };
	
	public State getLeftHandState();
	public State getRightHandState();
	
	public State getState();
}
