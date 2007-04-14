/* $Id$ */

package edu.uoregon.cs.p2presenter.philosopher.host;

import static java.lang.Math.PI;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;

import edu.uoregon.cs.p2presenter.interactivity.InteractivityView;
import edu.uoregon.cs.p2presenter.interactivity.InteractivityStateListener;
import edu.uoregon.cs.p2presenter.philosopher.Philosopher;
import edu.uoregon.cs.p2presenter.philosopher.Table;
import edu.uoregon.cs.p2presenter.philosopher.Philosopher.Hand;

public class PhilosopherVisualization extends JComponent implements InteractivityStateListener, InteractivityView<Table> {
	private static final long serialVersionUID = 1L;
	
	private static final Color TABLE_COLOR = new Color(204, 153, 102);
	private static final Color PLATE_COLOR = new Color(240, 255, 240);
	
	private static final Color HEAD_COLOR = Color.YELLOW;
	
	private static final Color WAITING_COLOR = Color.RED;
	private static final Color ACTIVE_COLOR = Color.GREEN;
	
	private int width, height;

	private static final double CENTER = 500;
	private static final double TABLE_DIAMETER = 736;
	private static final double TABLE_ORIGIN = 132;
	
	private static final double INACTIVE_SHOULDER_THETA = Math.PI / 3;
	private static final double INACTIVE_ELBOW_THETA = Math.PI / 3;
	
	private static final double WAITING_SHOULDER_THETA = Math.PI / 4;
	private static final double WAITING_ELBOW_THETA = Math.PI / 8;
	
	private static final double EATING_SHOULDER_THETA = Math.PI / 4;
	private static final double EATING_ELBOW_THETA = Math.PI / 4;
	
	private Shape tableShape;
	
	private Shape plate;
	private Shape chopstickOnTable;
	private Shape body;
	private Shape head;
	private ArmShapes leftArms = new ArmShapes();
	private ArmShapes rightArms = new ArmShapes();
	
	private Table table;

	@Override
	public void paintComponent(Graphics g) {
		if (tableShape == null) {
			calculateSizes();
		}
		
		Graphics2D g2 = (Graphics2D) g;
		
		float minDimension = Math.min(getWidth(), getHeight());
		
		width = getWidth();
		height = getHeight();
		
		g2.clearRect(0, 0, width, height);
		
		AffineTransform existingTransform = g2.getTransform();
		
		float scale = minDimension / 1000;

		g2.translate((width - minDimension) / 2, (height - minDimension) / 2);
		g2.scale(scale, scale);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setPaint(TABLE_COLOR);
		g2.fill(tableShape);
		
		double halfPhilosopherInterval = PI / table.getPhilosophers().size();
		double philosopherInterval = halfPhilosopherInterval * 2;
		
		for (Philosopher philosopher : table.getPhilosophers()) {
			g2.setPaint(PLATE_COLOR);
			g2.fill(plate);
			
			switch (philosopher.getState()) {
			case EATING:
				g2.setPaint(ACTIVE_COLOR);
				break;
			case WAITING:
				g2.setPaint(WAITING_COLOR);
				break;
			default:
				g2.setPaint(Color.BLUE);
				break;
			}
			
			g2.fill(body);
			
			drawArm(g2, philosopher.getLeftHand(), leftArms);

			drawArm(g2, philosopher.getRightHand(), rightArms);
			
			g2.setPaint(HEAD_COLOR);
			g2.fill(head);
			
			
			if (!philosopher.getLeftHand().getChopstick().isHeld()) {
				g2.rotate(halfPhilosopherInterval, CENTER, CENTER);
				
				g2.setPaint(Color.WHITE);
				g2.fill(chopstickOnTable);
				
				g2.rotate(halfPhilosopherInterval, CENTER, CENTER);
			}
			else {
				g2.rotate(philosopherInterval, CENTER, CENTER);
			}
		}
		
		g2.setTransform(existingTransform);
	}
	
	private static void drawArm(Graphics2D g2, Hand hand, ArmShapes arms) {
		Shape arm;
		if (hand.getState() == Hand.State.WAITING) {
			g2.setColor(WAITING_COLOR);
			arm = arms.waiting;
		}
		else if (hand.getState() == Hand.State.HOLDING) {
			g2.setColor(Color.WHITE);
			g2.fill(arms.heldChopstick);
			g2.setColor(ACTIVE_COLOR);
			arm = arms.eating;
		}
		else {
			g2.setColor(Color.BLUE);
			arm = arms.inactive;
		}
		g2.fill(arm);
	}

	public void stateChanged() {
		if (getWidth() > 0 || getHeight() > 0) {
			calculateSizes();
			repaint();
		}
	}
	
	private void calculateSizes() {
		tableShape = new Ellipse2D.Double(TABLE_ORIGIN, TABLE_ORIGIN, TABLE_DIAMETER, TABLE_DIAMETER);
		
		double plateDiameter = min(TABLE_DIAMETER / 4, TABLE_DIAMETER * 1.5 / table.getPhilosophers().size());
		double plateInset = plateDiameter / 4;
		
		double bodyWidth = plateDiameter * 1.3;
		double bodyHeight = plateDiameter / 3;
		double bodyRadius = bodyHeight * .8;
		
		double headWidth = bodyWidth / 3;
		double headHeight = bodyHeight * 2;
		double headRadius = headWidth * .8;
		
		double chopstickHeight = plateDiameter * .8;
		double chopstickWidth = plateDiameter / 30;
		
		double armHeight = bodyHeight * .6;
		double armWidth = bodyWidth / 2;
		double armRadius = armHeight;
		
		plate = new Ellipse2D.Double(CENTER - plateDiameter / 2, TABLE_ORIGIN + plateInset, plateDiameter, plateDiameter);
		
		body = new RoundRectangle2D.Double(CENTER - bodyWidth / 2, TABLE_ORIGIN - plateInset - bodyHeight, bodyWidth, bodyHeight, bodyRadius, bodyRadius);
		
		head = new RoundRectangle2D.Double(CENTER - headWidth / 2, TABLE_ORIGIN - plateInset - bodyHeight * 1.5, headWidth, headHeight, headRadius, headRadius);
		
		chopstickOnTable = new Rectangle2D.Double(CENTER - chopstickWidth / 2, TABLE_ORIGIN + plateInset, chopstickWidth, chopstickHeight);
		
		double upperArmX = CENTER + bodyWidth / 2;
		double armY = TABLE_ORIGIN - plateInset - bodyHeight / 2;
		double lowerArmX = upperArmX + armWidth;
		double heldChopstickX = lowerArmX + armWidth - armHeight / 2;
		double heldChopstickY = armY - armHeight / 2;
		
		AffineTransform transform = new AffineTransform();
		leftArms.waiting = generateLeftArm(upperArmX, armY, lowerArmX, armWidth, armHeight, armRadius, WAITING_SHOULDER_THETA, WAITING_ELBOW_THETA, transform);
		
		transform = new AffineTransform();
		leftArms.inactive = generateLeftArm(upperArmX, armY, lowerArmX, armWidth, armHeight, armRadius, INACTIVE_SHOULDER_THETA, INACTIVE_ELBOW_THETA, transform);
		
		transform = new AffineTransform();
		leftArms.eating = generateLeftArm(upperArmX, armY, lowerArmX, armWidth, armHeight, armRadius, EATING_SHOULDER_THETA, EATING_ELBOW_THETA, transform);
		leftArms.heldChopstick = new Rectangle2D.Double(heldChopstickX, heldChopstickY, chopstickWidth, chopstickHeight);
		leftArms.heldChopstick = transform.createTransformedShape(leftArms.heldChopstick);
		
		transform = AffineTransform.getTranslateInstance(2 * CENTER, 0);
		transform.scale(-1, 1);
		rightArms.inactive = transform.createTransformedShape(leftArms.inactive);
		rightArms.waiting = transform.createTransformedShape(leftArms.waiting);
		rightArms.eating = transform.createTransformedShape(leftArms.eating);
		rightArms.heldChopstick = transform.createTransformedShape(leftArms.heldChopstick);
	}
	
	private static Area generateLeftArm(double upperArmX, double armY, double lowerArmX, double armWidth, double armHeight, double armRadius, double shoulderTheta, double elbowTheta, AffineTransform transform) {
		Shape upperArm = new RoundRectangle2D.Double(upperArmX, armY, armWidth, armHeight, armRadius, armRadius);
		Shape lowerArm = new RoundRectangle2D.Double(lowerArmX, armY, armWidth, armHeight, armRadius, armRadius);
		
		transform.rotate(shoulderTheta, upperArmX, armY);
		Area result = new Area(transform.createTransformedShape(upperArm));
		transform.rotate(elbowTheta, lowerArmX, armY);
		result.add(new Area(transform.createTransformedShape(lowerArm)));
		
		return result;
	}
	
	private static class ArmShapes {
		private Shape inactive;
		private Shape waiting;
		private Shape eating;
		private Shape heldChopstick;
	}

	public void setModel(Table model) {
		this.table = model;
	}
}
