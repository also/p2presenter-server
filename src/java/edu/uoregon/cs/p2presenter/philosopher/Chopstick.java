/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

public class Chopstick {
	private Philosopher heldBy;
	
	private boolean held = false;
	
	public boolean isHeld() {
		return held;
	}
	
	public synchronized void hold(Philosopher philosopher) {
		if (held) {
			throw new IllegalStateException("Chopstick is already being held");
		}
		held = true;
		heldBy = philosopher;
	}
	
	public synchronized void release(Philosopher philosopher) {
		if (!held) {
			throw new IllegalStateException("Chopstick is not being held");
		}
		else if (philosopher != heldBy) {
			throw new IllegalStateException("Chopstick is being held by a different philosopher");
		}
		
		held = false;
		heldBy = null;
	}
	
}
