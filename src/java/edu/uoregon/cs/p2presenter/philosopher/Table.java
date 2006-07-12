/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table implements PhilosopherStateListener {
	private TableStateListener listener;
	private ArrayList<Philosopher> philosophers = new ArrayList<Philosopher>();
	
	public void setTableStateListener(TableStateListener listener) {
		this.listener = listener;
	}
	
	public void philosopherStateChanged(Philosopher philosopher) {
		listener.tableStateChanged(this);
	}
	
	public List<Philosopher> getPhilosophers() {
		return Collections.unmodifiableList(philosophers);
	}
	
	public int getPhilosopherCount() {
		return philosophers.size();
	}
	
	public PhilosopherController addPhilosopher() {
		PhilosopherController philosopher = new PhilosopherControllerImpl(this);
		
		philosophers.add(philosopher);
		assignChopsticks();
		
		return philosopher;
	}
	
	public void removePhilosopher(Philosopher philosopher) {
		philosophers.remove(philosopher);
		assignChopsticks();
	}
	
	private void assignChopsticks() {
		if (philosophers.size() > 0) {
			PhilosopherControllerImpl firstPhilosopher = (PhilosopherControllerImpl) philosophers.get(0);
			Chopstick firstChopstick = new Chopstick();
			Chopstick previousChopstick = firstChopstick;
			
			Chopstick currentChopstick;
			PhilosopherControllerImpl philosopher;
			
			int philosopherCount = philosophers.size();
			for (int i = 1; i < philosopherCount; i++) {
				currentChopstick = new Chopstick();
				philosopher = (PhilosopherControllerImpl) philosophers.get(i);
				philosopher.reset(currentChopstick, previousChopstick);
				previousChopstick = currentChopstick;
			}

			firstPhilosopher.reset(firstChopstick, previousChopstick);
		}
		
		listener.tableStateChanged(this);
	}
}
