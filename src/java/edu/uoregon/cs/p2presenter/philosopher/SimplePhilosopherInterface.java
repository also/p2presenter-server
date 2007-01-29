/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JButton;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityClientComponent;

import java.awt.GridBagConstraints;

public class SimplePhilosopherInterface extends JPanel implements InteractivityClientComponent<Philosopher>{

	private static final long serialVersionUID = 1L;
	private JButton takeLeftChopstickButton = null;
	private JButton takeRightChopstickButton = null;
	private JButton releaseRightChopstickButton = null;
	private JButton releaseLeftChopstickButton = null;
	
	private Philosopher philosopher;

	/**
	 * This is the default constructor
	 */
	public SimplePhilosopherInterface() {
		super();
		initialize();
	}
	
	public void setModel(Philosopher philosopher) {
		this.philosopher = philosopher;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.gridy = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getTakeLeftChopstickButton(), gridBagConstraints);
		this.add(getReleaseLeftChopstickButton(), gridBagConstraints1);
		this.add(getReleaseRightChopstickButton(), gridBagConstraints2);
		this.add(getTakeRightChopstickButton(), gridBagConstraints3);
	}

	/**
	 * This method initializes takeLeftChopstickButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getTakeLeftChopstickButton() {
		if (takeLeftChopstickButton == null) {
			takeLeftChopstickButton = new JButton();
			takeLeftChopstickButton.setText("Take Left");
			takeLeftChopstickButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					philosopher.getLeftHand().takeChopstick();
				}
			});
		}
		return takeLeftChopstickButton;
	}

	/**
	 * This method initializes takeRightChopstickButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getTakeRightChopstickButton() {
		if (takeRightChopstickButton == null) {
			takeRightChopstickButton = new JButton();
			takeRightChopstickButton.setText("Take Right");
			takeRightChopstickButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					philosopher.getRightHand().takeChopstick();
				}
			});
		}
		return takeRightChopstickButton;
	}

	/**
	 * This method initializes releaseRightChopstickButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getReleaseRightChopstickButton() {
		if (releaseRightChopstickButton == null) {
			releaseRightChopstickButton = new JButton();
			releaseRightChopstickButton.setText("Release Right");
			releaseRightChopstickButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							philosopher.getRightHand().releaseChopstick();
						}
					});
		}
		return releaseRightChopstickButton;
	}

	/**
	 * This method initializes releaseLeftChopstickButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getReleaseLeftChopstickButton() {
		if (releaseLeftChopstickButton == null) {
			releaseLeftChopstickButton = new JButton();
			releaseLeftChopstickButton.setText("Release Left");
			releaseLeftChopstickButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							philosopher.getLeftHand().releaseChopstick();
						}
					});
		}
		return releaseLeftChopstickButton;
	}

}
