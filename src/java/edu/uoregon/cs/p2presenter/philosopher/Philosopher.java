/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

public interface Philosopher {
	public enum State { EATING, WAITING, MEDITATING, INTERMEDIATE, INACTIVE }
	public enum HandState { HOLDING, WAITING, EMPTY }
	
	public HandState getLeftHandState();
	public HandState getRightHandState();
	
	public State getState();
}
