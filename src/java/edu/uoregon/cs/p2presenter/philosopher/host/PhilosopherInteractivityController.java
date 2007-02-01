/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher.host;

import java.awt.Container;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityController;
import edu.uoregon.cs.p2presenter.philosopher.Philosopher;
import edu.uoregon.cs.p2presenter.philosopher.PhilosopherVisualization;
import edu.uoregon.cs.p2presenter.philosopher.Table;

public class PhilosopherInteractivityController implements InteractivityController<Philosopher> {
	private PhilosopherVisualization view;
	private Table table;
	
	public PhilosopherInteractivityController() {
		table = new Table();
		view = new PhilosopherVisualization(table);
	}
	
	public Container getView() {
		return view;
	}

	public Philosopher onConnect() {
		return table.addPhilosopher();
	}

	public void onDisconnect(Philosopher philosopher) {
		table.removePhilosopher(philosopher);
	}

}
