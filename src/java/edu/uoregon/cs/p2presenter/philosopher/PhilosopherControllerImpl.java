/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

public class PhilosopherControllerImpl implements Philosopher {
	private Philosopher philosopher = this;
	
	private Table table;
	
	public PhilosopherControllerImpl(Table table) {
		this.table = table;
	}
	
	public void reset(Chopstick leftChopstick, Chopstick rightChopstick) {
		leftHand = new HandImpl(leftChopstick);
		rightHand = new HandImpl(rightChopstick);
		stateChanged();
	}
	
	private HandImpl leftHand;
	private HandImpl rightHand;
	
	private class HandImpl implements Hand {
		private HandImpl(Chopstick chopstick) {
			this.chopstick = chopstick;
		}
		private State state = State.EMPTY;
		private Chopstick chopstick;
		
		public State getState() {
			return state;
		}
		
		public Chopstick getChopstick() {
			return chopstick;
		}
		
		public State takeChopstick() {
			synchronized (chopstick) {
				if (state == State.EMPTY) {
					if (!chopstick.isHeld()) {
						chopstick.hold(philosopher);
						state = State.HOLDING;
						stateChanged();
						
					}
					else {
						state = State.WAITING;
						stateChanged();
						new WaitingHandThread().start();
					}
				}
			}
			
			return state;
		}
		
		public synchronized void releaseChopstick() {
			if (state == State.HOLDING) {
				chopstick.release(philosopher);
				state = State.EMPTY;
				stateChanged();
			}
		}
		
		private class WaitingHandThread extends Thread {
						
			@Override
			public void run() {
				synchronized (chopstick) {
					while (chopstick.isHeld()) {
						try {
							chopstick.wait();
						}
						catch (InterruptedException ex) {
							// TODO maybe do something
						}
					}
					
					chopstick.hold(philosopher);
					
					state = Hand.State.HOLDING;
						
					stateChanged();
				}
			}
		}
		
	}
	
	public void makeInactive() {
		
	}

	public synchronized State getState() {
		if (leftHand.state == Hand.State.WAITING || rightHand.state == Hand.State.WAITING) {
			return State.WAITING;
		}
		else if (leftHand.state == rightHand.state) {
			switch (leftHand.state) {
			case EMPTY:
				return State.MEDITATING;
			case HOLDING:
				return State.EATING;
			}
		}
		
		return State.INTERMEDIATE;
	}
	
	public Hand getLeftHand() {
		return leftHand;
	}
	
	public Hand getRightHand() {
		return rightHand;
	}
	
	private void stateChanged() {
		table.philosopherStateChanged(this);
	}
}
