/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.demo;

import java.awt.Dimension;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityParticipantClient;

public class DemoClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("Interactivity Demo Participant");
		String host = args.length == 0 ? JOptionPane.showInputDialog("Host address:", "localhost") : args[0];
		if (host == null) {
			System.exit(0);
		}
		String interactivityIdString = JOptionPane.showInputDialog("Interactivity Id:", 1);
		if (interactivityIdString == null) {
			System.exit(0);
		}
		try {
			LocalConnection connection = new LocalConnection(new Socket(host, 9000));
			connection.start();
			
			InteractivityParticipantClient client = new InteractivityParticipantClient(connection, new Integer(interactivityIdString));
			
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
