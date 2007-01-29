/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher.host;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityRunner;
import edu.uoregon.cs.p2presenter.philosopher.Philosopher;
import edu.uoregon.cs.p2presenter.philosopher.Table;

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
