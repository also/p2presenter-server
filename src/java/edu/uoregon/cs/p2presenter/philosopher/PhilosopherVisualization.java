/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import static java.lang.Math.PI;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import javax.swing.JComponent;

public class PhilosopherVisualization extends JComponent implements ActionListener, TableStateListener {
	private static final int INSET = 40;
	
	private static final Color TABLE_COLOR = new Color(204, 153, 102);
	private static final Color PLATE_COLOR = new Color(240, 255, 240);
	
	private List<Philosopher> philosophers;
	
	public PhilosopherVisualization(Table table) {
		table.setTableStateListener(this);
		philosophers = table.getPhilosophers();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int width = getWidth();
		int height = getHeight();
		g2.clearRect(0, 0, width, height);
		
		float minDimension = Math.min(width, height);
		
		double tableDiameter = (minDimension - INSET * 2) * .8;
		double tableRadius = tableDiameter / 2;
		
		double centerX = width / 2;
		double centerY = height / 2;
		
		double tableOriginX = centerX - tableRadius;
		double tableOriginY = centerY - tableRadius;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setPaint(TABLE_COLOR);
		g2.fill(new Ellipse2D.Double(tableOriginX, tableOriginY, tableDiameter, tableDiameter));
		
		double halfPhilosopherInterval = PI / philosophers.size();
		
		double plateDiameter = min(tableDiameter / 4, tableDiameter * 1.5 / philosophers.size());
		double plateInset = plateDiameter / 4;
		
		double philosopherWidth = plateDiameter * 1.3;
		double philosopherHeight = plateDiameter / 3;
		
		double chopstickHeight = plateDiameter * .8;
		double chopstickWidth = plateDiameter / 30;
		double philosopherRadius = philosopherHeight * .8;
		
		//double armHeight = philosopherHeight * .6;
		//double armWidth = philosopherWidth /2;
		//double armRadius = armHeight;
		
		
		AffineTransform transform;
		
		
		Ellipse2D plate = new Ellipse2D.Double(centerX - plateDiameter / 2, tableOriginY + plateInset, plateDiameter, plateDiameter);
		
		RoundRectangle2D philosopherBody = new RoundRectangle2D.Double(centerX - philosopherWidth / 2, tableOriginY - plateInset - philosopherHeight, philosopherWidth, philosopherHeight, philosopherRadius, philosopherRadius);
		
		//RoundRectangle2D philosopherUpperArm = new RoundRectangle2D.Double(centerX + philosopherWidth / 2, tableOriginY - plateInset - philosopherHeight / 2, armWidth, armHeight, armRadius, armRadius);
		
		//Shape philosopherArm = AffineTransform.getRotateInstance(1, philosopherUpperArm.getX(), philosopherUpperArm.getY()).createTransformedShape(philosopherUpperArm);
		
		Rectangle2D.Double chopstick = new Rectangle2D.Double(centerX - chopstickWidth / 2, tableOriginY + plateInset, chopstickWidth, chopstickHeight);
		
		double theta = 0;

		for (Philosopher philosopher : philosophers) {
			transform = AffineTransform.getRotateInstance(theta, centerX, centerY);
			
			g2.setPaint(PLATE_COLOR);
			g2.fill(transform.createTransformedShape(plate));
			
			g2.setPaint(Color.BLUE);
			g2.fill(transform.createTransformedShape(philosopherBody));
			//g2.fill(transform.createTransformedShape(philosopherArm));
			
			theta += halfPhilosopherInterval;
			transform = AffineTransform.getRotateInstance(theta, centerX, centerY);
			
			g2.setPaint(Color.WHITE);
			g2.fill(transform.createTransformedShape(chopstick));
			
			theta += halfPhilosopherInterval;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
	}

	public void tableStateChanged(Table table) {
		philosophers = table.getPhilosophers();
		repaint();
	}
	
	
}
