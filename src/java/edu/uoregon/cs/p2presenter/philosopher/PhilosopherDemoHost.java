/* $Id: PhilosopherDemo.java 86 2007-01-25 09:23:58Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.p2presenter.philosopher;

import java.awt.Dimension;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityRequestMatcher;
import edu.uoregon.cs.p2presenter.remoting.InvocationRequestHandler;

public class PhilosopherDemoHost {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		String host = args.length == 0 ? JOptionPane.showInputDialog("Host address:", "localhost") : args[0];
		if (host == null) {
			System.exit(0);
		}

		Connection connection = new Connection(new Socket(host, 9000));
		connection.start();
		
		Table table = new Table();
		PhilosopherVisualization viz = new PhilosopherVisualization(table);
		
		JFrame frame = new JFrame("Dining Philosophers Demo");
		frame.setContentPane(viz);
		frame.setSize(new Dimension(500, 500));
		frame.setVisible(true);

		PhilosopherInteractivityRunner philosopherInteractivityRunner = new PhilosopherInteractivityRunner(table);
		InvocationRequestHandler invoker = new InvocationRequestHandler();
		connection.getRequestHandlerMapping().mapHandler(new InteractivityRequestMatcher("controller"), invoker);
		connection.setAttribute("interactivity", philosopherInteractivityRunner);
	}
}
