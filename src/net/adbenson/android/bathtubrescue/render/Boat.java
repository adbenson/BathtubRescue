package net.adbenson.android.bathtubrescue.render;

import java.util.LinkedList;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Camera.Area;


public class Boat implements DrawingQueueable, AbstractModelRender{
		
	private static final double FRICTION = 0.95;
	private static final double MAX_TURN_RADIUS = 0.5;
	private static final double MAX_ACCELERATION = 2.0;
	private static final int PASSENGER_SPACE = 30;
	private static final int PASSENGER_VERTICAL_OFFSET = -10;
	
	private static final int BOW_DISTANCE = 20;

	Vector position;
	Vector trajectory;
	float acceleration;
	float accelRate;
	
	private Path prototype;
	private Region shape;
	
	private Fire fire;
	
	private boolean crashed;
	
	private boolean debug = false;
	
	private PullString string;
	
	private LinkedList<Person> passengers;	
	
	public Boat(int posX, int posY) {
		trajectory = new Vector(-0.001, 0); //Just enough to establish direction
		acceleration = 0;
		position = new Vector(posX, posY);
		
		prototype = generateProtoShape();
//		prototype.transform(AffineTransform.getScaleInstance(1.5, 1.5));
		translateShape();
		
		string = new PullString();
		string.setStart(getBow());
		string.setTrail(trajectory);
		string.setEnd(string.getTrail());
		string.drop();
		
		crashed = false;
		
		fire = new Fire();
		passengers = new LinkedList<Person>();
	}
	
	public void crash() {
		trajectory = new Vector(0, 0);
		string.drop();
		crashed = true;
	}

	public Rect getBounds() {
		Rect bounds = shape.getBounds();
		return bounds;
	}
	
	protected Path generateProtoShape() {
		Path shape = new Path();
		shape.moveTo(-10, -20);
		shape.lineTo(10, -20);
		shape.lineTo(10, 5);
		
		shape.quadTo(10, 15, 0, 20);		
		shape.quadTo(-10, 15, -10, 5);
		
		shape.lineTo(-10, -20);
		shape.close();
		
		return shape;
	}
	
	public boolean isStringHeld() {
		return string.isHeld();
	}
	
	public Vector getPosition() {
		return position;
	}

	public void move() {
		position = position.add(trajectory);
		trajectory = trajectory.scale(FRICTION);
				
		translateShape();
		
        if (! passengers.isEmpty()) { 
	        int space = PASSENGER_SPACE / passengers.size();
	        int offset = -(PASSENGER_SPACE / 2);
	        
	        for (Person p : passengers) {
	        	p.setLocation(position.add(new Vector(offset, PASSENGER_VERTICAL_OFFSET)));
	        	offset += space;
	        }
        }
        
		string.setTrail(trajectory);
        
        if (!crashed) {
    		string.setStart(getBow());
        }
        
        fire.setPosition(position);
	}
		
	public void pull(Handle handle) {
		if (string.isHeld()) {
			string.setEnd(handle);
			
			Vector pull = handle.subtract(position);
			double pullDistance = pull.magnitude();
			
			double pullForce = string.pull(pullDistance);
			
			//Max acceleration decreases at half the rate of passenger increase
			double accelCap = MAX_ACCELERATION / (passengers.size()/2);
			pullForce = Math.max(0, Math.min(pullForce, accelCap)); //Constrain the force to the max acceleration
			
			Vector pullDirection = pull.normalize();
//			Vector boatOrientation = trajectory.normalize();
//			Vector turn = pullDirection.subtract(boatOrientation);
//			double turnRadius = turn.magnitude();
//	System.out.println(turnRadius);
//			if (turnRadius > MAX_TURN_RADIUS) {
//	System.out.println("Capping turn");
//				double reduction = MAX_TURN_RADIUS / turnRadius;
//				turn = turn.scale(reduction);
////				pullDirection = boatOrientation.add(turn);
//			}
//	System.out.println(turn.magnitude());
			Vector change = pullDirection.scale(pullForce);
			
			Vector newTrajectory = trajectory.add(change); 
			double turnRadius = newTrajectory.normalDistance(trajectory);
//			System.out.println("A"+turnRadius);
			
			trajectory = newTrajectory;			
		}
		else if (handle.contains(string.getTrail())) {
			string.grab();
		}
	}
	
	public void translateShape() {
//		AffineTransform tx = new AffineTransform();
//		tx.translate(position.x, position.y);
//		tx.rotate(trajectory.getAngle());
//		shape = new Area(tx.createTransformedShape(prototype));
	}
	
	public void enqueueForDraw(DrawingQueue queue) {
		if (!crashed) {
			queue.add(string);
		}
		else {
			queue.add(fire);
		}

		queue.add(passengers);
		
		queue.add(new Drawable(-10) {
			@Override
			public void draw(Canvas g) {		
		        g.setColor(Color.BLACK);
		        g.fill(shape);
		                      
		        if (debug) {
//			        g.setColor(Color.pink);
//			        g.setStroke(new BasicStroke(3));
//			        Vector localTrajectory = trajectory.scale(5).add(position);
//			        g.drawLine(position.intX(), position.intY(), localTrajectory.intX(), localTrajectory.intY());
//			        
//			        g.draw(getBounds());
		        }
			}
		});
	}
	
	public Vector getBow() {
		return position.add(trajectory.normalize().scale(BOW_DISTANCE));
	}

//	public Shape getShape() {
//		return shape;
//	}

	public boolean intersects(Person p) {
		Rectangle personBounds = p.getBounds();
		//Coarse test
		if (! getBounds().intersects(p.getBounds())) {
			return false;
		}
		
		//Fine test
		if (shape.intersects(personBounds)) {
			return true;
		}
		else {
			return false;
		}
		
	}

	public void pickup(Person p) {
		System.out.println("Picked up passenger");
		passengers.add(p);
		p.setInWater(false);
		Collections.sort(passengers);
	}
}