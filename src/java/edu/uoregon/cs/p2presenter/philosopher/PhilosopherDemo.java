/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

import edu.uoregon.cs.p2presenter.ConnectionManager;
import edu.uoregon.cs.p2presenter.server.P2PresenterServerPortListener;

public class PhilosopherDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Table table = new Table();
		PhilosopherVisualization viz = new PhilosopherVisualization(table);
		
		JFrame frame = new JFrame("Dining Philosophers Demo");
		frame.setContentPane(viz);
		frame.setSize(new Dimension(500, 500));
		frame.setVisible(true);
		
		ConnectionManager server = new PhilosopherConnectionManager(table);
		P2PresenterServerPortListener portListener = new P2PresenterServerPortListener(9000, server);
		portListener.run();
	}

}
