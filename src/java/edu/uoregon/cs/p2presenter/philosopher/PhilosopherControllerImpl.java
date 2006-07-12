/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

public class PhilosopherControllerImpl implements PhilosopherController {
	private Philosopher philosopher = this;
	
	private Table table;
	
	public PhilosopherControllerImpl(Table table) {
		this.table = table;
	}
	
	public void reset(Chopstick leftChopstick, Chopstick rightChopstick) {
		left = new Hand(leftChopstick);
		right = new Hand(rightChopstick);
		stateChanged();
	}
	
	private Hand left;
	private Hand right;
	
	private class Hand {
		private Hand(Chopstick chopstick) {
			this.chopstick = chopstick;
		}
		private State state = State.MEDITATING;
		private Chopstick chopstick;
	}
	
	public void releaseLeftChopstick() {
		releaseChopstick(left);
	}

	public void releaseRightChopstick() {
		releaseChopstick(right);
	}
	
	private synchronized void releaseChopstick(Hand hand) {
		if (hand.state == State.EATING) {
			hand.chopstick.release(this);
			hand.state = State.MEDITATING;
			stateChanged();
		}
	}

	public State takeLeftChopstick() {
		return takeChopstick(left);
	}

	public State takeRightChopstick() {
		return takeChopstick(right);
	}
	
	private State takeChopstick(Hand hand) {
		synchronized (hand.chopstick) {
			if (!hand.chopstick.isHeld()) {
				hand.chopstick.hold(this);
				hand.state = State.EATING;
				stateChanged();
				
			}
			else {
				new HolderThread(hand).start();
			}
		}
		
		return hand.state;
	}

	public State getLeftHandState() {
		return left.state;
	}

	public State getRightHandState() {
		return right.state;
	}

	public synchronized State getState() {
		// TODO needs some synchronization
		if (left.state == right.state) {
			return left.state;
		}
		else if (left.state == State.WAITING || right.state == State.WAITING) {
			return State.WAITING;
		}
		else {
			return State.INTERMEDIATE;
		}
	}
	
	private void stateChanged() {
		table.philosopherStateChanged(this);
	}
	
	private class HolderThread extends Thread {
		private Hand hand;
		
		private HolderThread(Hand hand) {
			this.hand = hand;
		}
		
		@Override
		public void run() {
			synchronized (hand.chopstick) {
				hand.state = Philosopher.State.WAITING;
				stateChanged();
				
				while (hand.chopstick.isHeld()) {
					try {
						hand.chopstick.wait();
					}
					catch (InterruptedException ex) {
						// TODO maybe do something
					}
				}
				
				hand.chopstick.hold(philosopher);
				
				hand.state = Philosopher.State.EATING;
					
				stateChanged();
			}
		}
	}

}
