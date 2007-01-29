/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import java.awt.Dimension;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityClient;

public class PhilosopherDemoClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("Interactivity Controller");
		String host = args.length == 0 ? JOptionPane.showInputDialog("Host address:", "localhost") : args[0];
		if (host == null) {
			System.exit(0);
		}
		try {
			Connection connection = new Connection(new Socket(host, 9000));
			connection.start();
			
			InteractivityClient client = new InteractivityClient(connection, 0);
			
			// TODO
			frame.setContentPane(client.getView());
			frame.setSize(new Dimension(300, 200));
			frame.setVisible(true);
		}
		catch (ConnectException ex) {
			JOptionPane.showMessageDialog(null, "Couldn't connect to server '" + host + "'");
		}
	}

}
