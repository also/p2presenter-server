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
		private HandState state = HandState.EMPTY;
		private Chopstick chopstick;
	}
	
	public void releaseLeftChopstick() {
		releaseChopstick(left);
	}

	public void releaseRightChopstick() {
		releaseChopstick(right);
	}
	
	private synchronized void releaseChopstick(Hand hand) {
		if (hand.state == HandState.HOLDING) {
			hand.chopstick.release(this);
			hand.state = HandState.EMPTY;
			stateChanged();
		}
	}

	public HandState takeLeftChopstick() {
		return takeChopstick(left);
	}

	public HandState takeRightChopstick() {
		return takeChopstick(right);
	}
	
	private HandState takeChopstick(Hand hand) {
		synchronized (hand.chopstick) {
			if (!hand.chopstick.isHeld()) {
				hand.chopstick.hold(this);
				hand.state = HandState.HOLDING;
				stateChanged();
				
			}
			else {
				hand.state = HandState.WAITING;
				stateChanged();
				new HolderThread(hand).start();
			}
		}
		
		return hand.state;
	}

	public HandState getLeftHandState() {
		return left.state;
	}

	public HandState getRightHandState() {
		return right.state;
	}

	public synchronized State getState() {
		if (left.state == HandState.WAITING || right.state == HandState.WAITING) {
			return State.WAITING;
		}
		else if (left.state == right.state) {
			switch (left.state) {
			case EMPTY:
				return State.MEDITATING;
			case HOLDING:
				return State.EATING;
			}
		}
		
		return State.INTERMEDIATE;
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
				while (hand.chopstick.isHeld()) {
					try {
						hand.chopstick.wait();
					}
					catch (InterruptedException ex) {
						// TODO maybe do something
					}
				}
				
				hand.chopstick.hold(philosopher);
				
				hand.state = HandState.HOLDING;
					
				stateChanged();
			}
		}
	}

}
