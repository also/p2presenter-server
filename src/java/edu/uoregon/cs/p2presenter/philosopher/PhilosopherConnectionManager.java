/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import bsh.EvalError;
import bsh.Interpreter;
import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionManager;
import edu.uoregon.cs.p2presenter.jsh.JshRequestHandler;

public class PhilosopherConnectionManager extends ConnectionManager {
	private Table table;
	
	public PhilosopherConnectionManager(Table table) {
		this.table = table;
	}
	
	@Override
	protected void connectionCreatedInternal(Connection connection) {
		Philosopher philosopher = table.addPhilosopher();
		connection.setProperty("philosopher", philosopher);
		Interpreter interpreter = JshRequestHandler.getInterpreter(connection);
		try {
			interpreter.set("philosopher", philosopher);
		}
		catch (EvalError ex) {
			// FIXME
			throw new Error(ex);
		}
	}
	
	@Override
	protected void connectionClosedInternal(Connection connection) {
		table.removePhilosopher((Philosopher) connection.getProperty("philosopher"));
	}
}
