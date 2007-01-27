/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import java.awt.Dimension;

import javax.swing.JFrame;

public class PhilosopherVisualizer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Table table = new Table();
		PhilosopherVisualization viz = new PhilosopherVisualization(table);
		
		for (int i = 0; i < 10; i++) {
			table.addPhilosopher();
		}
		JFrame frame = new JFrame();
		frame.setContentPane(viz);
		frame.setSize(new Dimension(500, 500));
		frame.setVisible(true);
	}

}
