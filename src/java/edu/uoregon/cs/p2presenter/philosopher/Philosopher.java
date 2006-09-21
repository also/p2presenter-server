/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

public interface Philosopher {
	public interface Hand {
		public enum State { HOLDING, WAITING, EMPTY }
		
		public State getState();
		
		public Chopstick getChopstick();
		
		public State takeChopstick();
		public void releaseChopstick();
	}
	
	public enum State { EATING, WAITING, MEDITATING, INTERMEDIATE, INACTIVE }

	public Hand getLeftHand();
	public Hand getRightHand();
	
	public State getState();
}
