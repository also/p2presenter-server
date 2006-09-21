/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import java.awt.Dimension;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.uoregon.cs.p2presenter.Connection;
import edu.uoregon.cs.p2presenter.ConnectionManager;
import edu.uoregon.cs.p2presenter.jsh.JshClient;

public class PhilosopherClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("Philosopher");
		String host = args.length == 0 ? JOptionPane.showInputDialog("Host address:", "localhost") : args[0];
		if (host == null) {
			System.exit(0);
		}
		try {
			Socket socket = new Socket(host, 9000);

			Connection connection = new ConnectionManager().createConnection(socket);
			
			connection.start();
			
			JshClient jshClient = new JshClient(connection);
			
			Philosopher philosopher = jshClient.proxy(Philosopher.class, "philosopher");
			
			SimplePhilosopherInterface philosopherInterface = new SimplePhilosopherInterface();
			philosopherInterface.setPhilosopher(philosopher);
			
			frame.setContentPane(philosopherInterface);
			frame.setSize(new Dimension(300, 200));
			frame.setVisible(true);
		}
		catch (ConnectException ex) {
			JOptionPane.showMessageDialog(null, "Couldn't connect to server '" + host + "'");
		}
	}

}
