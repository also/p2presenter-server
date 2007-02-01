/* $Id: PhilosopherDemo.java 86 2007-01-25 09:23:58Z rberdeen@cs.uoregon.edu $ */

package edu.uoregon.cs.p2presenter.interactivity.demo;

import java.awt.Dimension;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityHostClient;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityRequestMatcher;
import edu.uoregon.cs.p2presenter.remoting.InvocationRequestHandler;

public class PhilosopherDemoHost {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String host = args.length == 0 ? JOptionPane.showInputDialog("Host address:", "localhost") : args[0];
		if (host == null) {
			System.exit(0);
		}
		try {
			Connection connection = new Connection(new Socket(host, 9000));
			connection.start();
	
			InteractivityHostClient client = new InteractivityHostClient(connection, 0);
			
			JFrame frame = new JFrame("Interactivity Demo Host");
			frame.setContentPane(client.getController().getView());
			frame.setSize(new Dimension(500, 500));
			frame.setVisible(true);
	
			InvocationRequestHandler invoker = new InvocationRequestHandler();
			connection.getRequestHandlerMapping().mapHandler(new InteractivityRequestMatcher("controller"), invoker);
			connection.setAttribute("interactivity", client.getController());
			client.begin();
		}
		catch (ConnectException ex) {
			JOptionPane.showMessageDialog(null, "Couldn't connect to server '" + host + "'");
		}
	}
}
