/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uoregon.cs.p2presenter.philosopher.host.PhilosopherControllerImpl;
import edu.uoregon.cs.p2presenter.philosopher.host.PhilosopherStateListener;
import edu.uoregon.cs.p2presenter.philosopher.host.TableStateListener;

public class Table implements PhilosopherStateListener {
	private TableStateListener listener;
	private ArrayList<PhilosopherControllerImpl> philosophers = new ArrayList<PhilosopherControllerImpl>();
	
	private List<? extends Philosopher> unmodifiablePhilosophers = Collections.unmodifiableList(philosophers);
	
	public void setTableStateListener(TableStateListener listener) {
		this.listener = listener;
	}
	
	public void philosopherStateChanged(Philosopher philosopher) {
		listener.tableStateChanged(this);
	}
	
	@SuppressWarnings("unchecked")
	public List<Philosopher> getPhilosophers() {
		// the list is unmodifiable so we don't have to worry about other subclasses being inserted
		return (List<Philosopher>) unmodifiablePhilosophers;
	}
	
	public int getPhilosopherCount() {
		return philosophers.size();
	}
	
	public Philosopher addPhilosopher() {
		PhilosopherControllerImpl philosopher = new PhilosopherControllerImpl(this);
		
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
