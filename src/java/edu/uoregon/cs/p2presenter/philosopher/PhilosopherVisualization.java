/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher;

import static java.lang.Math.PI;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import javax.swing.JComponent;

public class PhilosopherVisualization extends JComponent implements ActionListener, TableStateListener {
	private static final long serialVersionUID = 1L;

	private static final int INSET = 40;
	
	private static final Color TABLE_COLOR = new Color(204, 153, 102);
	private static final Color PLATE_COLOR = new Color(240, 255, 240);
	
	private int width, height;

	private double centerX, centerY;
	
	private Shape table;
	
	private Shape plate;
	private Shape chopstick;
	private Shape body;
	private Shape head;
	
	private List<Philosopher> philosophers;
	
	public PhilosopherVisualization(Table table) {
		table.setTableStateListener(this);
		philosophers = table.getPhilosophers();
		calculateSizes();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		if (width != getWidth() || height != getHeight()) {
			calculateSizes();
		}
		
		width = getWidth();
		height = getHeight();
		
		g2.clearRect(0, 0, width, height);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setPaint(TABLE_COLOR);
		g2.fill(table);
		
		double halfPhilosopherInterval = PI / philosophers.size();
		double philosopherInterval = halfPhilosopherInterval * 2;
		
		AffineTransform existingTransform = g2.getTransform();
		
		for (Philosopher philosopher : philosophers) {
			g2.setPaint(PLATE_COLOR);
			g2.fill(plate);
			
			switch (philosopher.getState()) {
			case EATING:
				g2.setPaint(Color.GREEN);
				break;
			case WAITING:
				g2.setPaint(Color.RED);
				break;
			default:
				g2.setPaint(Color.BLUE);
				break;
			}
			
			g2.fill(body);
			//g2.fill(philosopherArm);
			
			g2.setPaint(Color.YELLOW);
			g2.fill(head);
			
			
			if (!philosopher.getLeftHand().getChopstick().isHeld()) {
				g2.rotate(halfPhilosopherInterval, centerX, centerY);
				
				g2.setPaint(Color.WHITE);
				g2.fill(chopstick);
				
				g2.rotate(halfPhilosopherInterval, centerX, centerY);
			}
			else {
				g2.rotate(philosopherInterval, centerX, centerY);
			}
		}
		
		g2.setTransform(existingTransform);
	}
	
	public void actionPerformed(ActionEvent e) {
	}

	public void tableStateChanged(Table table) {
		philosophers = table.getPhilosophers();
		
		if (getWidth() > 0 || getHeight() > 0) {
			calculateSizes();
			repaint();
		}
	}
	
	private void calculateSizes() {
		float minDimension = Math.min(getWidth(), getHeight());
		
		double tableDiameter = (minDimension - INSET * 2) * .8;
		double tableRadius = tableDiameter / 2;
		
		centerX = getWidth() / 2;
		centerY = getHeight() / 2;
		
		double tableOriginX = centerX - tableRadius;
		double tableOriginY = centerY - tableRadius;
		
		table = new Ellipse2D.Double(tableOriginX, tableOriginY, tableDiameter, tableDiameter);
		
		double plateDiameter = min(tableDiameter / 4, tableDiameter * 1.5 / philosophers.size());
		double plateInset = plateDiameter / 4;
		
		double bodyWidth = plateDiameter * 1.3;
		double bodyHeight = plateDiameter / 3;
		double bodyRadius = bodyHeight * .8;
		
		double headWidth = bodyWidth / 3;
		double headHeight = bodyHeight * 2;
		double headRadius = headWidth * .8;
		
		double chopstickHeight = plateDiameter * .8;
		double chopstickWidth = plateDiameter / 30;
		
		//double armHeight = philosopherHeight * .6;
		//double armWidth = philosopherWidth /2;
		//double armRadius = armHeight;
		
		plate = new Ellipse2D.Double(centerX - plateDiameter / 2, tableOriginY + plateInset, plateDiameter, plateDiameter);
		
		body = new RoundRectangle2D.Double(centerX - bodyWidth / 2, tableOriginY - plateInset - bodyHeight, bodyWidth, bodyHeight, bodyRadius, bodyRadius);
		
		head = new RoundRectangle2D.Double(centerX - headWidth / 2, tableOriginY - plateInset - bodyHeight * 1.5, headWidth, headHeight, headRadius, headRadius);
		
		//RoundRectangle2D philosopherUpperArm = new RoundRectangle2D.Double(centerX + philosopherWidth / 2, tableOriginY - plateInset - philosopherHeight / 2, armWidth, armHeight, armRadius, armRadius);
		
		//Shape philosopherArm = AffineTransform.getRotateInstance(1, philosopherUpperArm.getX(), philosopherUpperArm.getY()).createTransformedShape(philosopherUpperArm);
		
		chopstick = new Rectangle2D.Double(centerX - chopstickWidth / 2, tableOriginY + plateInset, chopstickWidth, chopstickHeight);
	}
}
