/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityRunner;

public class PhilosopherInteractivityRunner implements InteractivityRunner<Philosopher> {
	private Table table;
	
	public PhilosopherInteractivityRunner(Table table) {
		this.table = table;
	}

	public Philosopher onConnect() {
		return table.addPhilosopher();
	}

	public void onDisconnect(Philosopher philosopher) {
		table.removePhilosopher(philosopher);
	}

}
