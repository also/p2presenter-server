/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher.host;

import java.awt.Dimension;

import javax.swing.JFrame;

import edu.uoregon.cs.p2presenter.philosopher.Table;

public class PhilosopherVisualizer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		Table table = new Table();
		PhilosopherVisualization viz = new PhilosopherVisualization();
		JFrame frame = new JFrame();
		frame.setContentPane(viz);
		frame.setSize(new Dimension(500, 500));
		frame.setVisible(true);
		
		for (int i = 0; i < 1; i++) {
			table.addPhilosopher();
			Thread.sleep(300);
		}
	}

}
