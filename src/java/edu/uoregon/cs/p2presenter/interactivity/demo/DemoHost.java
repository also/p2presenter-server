/* $Id$ */

package edu.uoregon.cs.p2presenter.interactivity.demo;

import java.awt.Dimension;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.uoregon.cs.p2presenter.LocalConnection;
import edu.uoregon.cs.p2presenter.interactivity.host.InteractivityHostClient;
import edu.uoregon.cs.p2presenter.remoting.InvocationRequestHandler;

public class DemoHost {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
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
			
			Integer interactivityId = new Integer(interactivityIdString);
	
			InteractivityHostClient client = new InteractivityHostClient(connection, interactivityId);
			
			JFrame frame = new JFrame("Interactivity Demo Host");
			frame.setContentPane(client.getController().getView());
			frame.setSize(new Dimension(500, 500));
			frame.setVisible(true);
	
			InvocationRequestHandler invoker = new InvocationRequestHandler();
			connection.getRequestHandlerMapping().mapHandler("/interactivity/" + interactivityId + "/controller", invoker);
			connection.setAttribute("interactivity", client.getController());
			client.begin();
		}
		catch (ConnectException ex) {
			JOptionPane.showMessageDialog(null, "Couldn't connect to server '" + host + "'");
		}
	}
}
