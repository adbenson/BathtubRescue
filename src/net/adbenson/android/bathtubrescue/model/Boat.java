package net.adbenson.android.bathtubrescue.model;

import java.util.Collections;
import java.util.LinkedList;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;

public class Boat implements DrawingQueueable{
		
	private static final float FRICTION = 0.98f;
	private static final float MAX_TURN_RADIUS = 0.5f;
	private static final float MAX_ACCELERATION = 2.0f;
	private static final int PASSENGER_SPACE = 30;
	private static final int PASSENGER_VERTICAL_OFFSET = -10;

	private static final int BOW_DISTANCE = 20;
	
	private net.adbenson.android.bathtubrescue.render.Boat render;

	Vector position;
	Vector trajectory;
	float acceleration;
	float accelRate;
	
	private Fire fire;
	
	private boolean crashed;
	
	private boolean debug = false;
	
	private PullString string;
	
	private LinkedList<Person> passengers;	
	
	public Boat(int posX, int posY) {
		trajectory = new Vector(-0.001, 0); //Just enough to establish direction
		acceleration = 0;
		position = new Vector(posX, posY);
		
		string = new PullString();
		string.setStart(getBow());
		string.setTrail(trajectory);
		string.setEnd(string.getTrail());
		string.drop();
		
		crashed = false;
		
		fire = new Fire();
		passengers = new LinkedList<Person>();
		
		render = new net.adbenson.android.bathtubrescue.render.Boat();
	}
	
	public void crash() {
		trajectory = new Vector(0, 0);
		string.drop();
		crashed = true;
	}

	public Rect getBounds() {
		Rect bounds = render.getBounds();
		bounds.offsetTo(position.intX(), position.intY());
		return bounds;
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
				
		render.translateShape();
		
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
			float pullDistance = pull.magnitude();
			
			float pullForce = string.pull(pullDistance);
			
			//Max acceleration decreases at half the rate of passenger increase
			float accelCap = MAX_ACCELERATION / (passengers.size()/2f);
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
	
	public void enqueueForDraw(DrawingQueue queue) {
//		if (!crashed) {
			queue.add(string);
//		}
//		else {
//			queue.add(fire);
//		}

//		queue.add(passengers);
		
		queue.add(new Drawable(-10) {
			@Override
			public void draw(Canvas g) {
				g.translate(position.x, position.y);
				render.draw(g);
		                      
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
//		Rectangle personBounds = p.getBounds();
//		//Coarse test
//		if (! getBounds().intersects(p.getBounds())) {
//			return false;
//		}
//		
//		//Fine test
//		if (shape.intersects(personBounds)) {
//			return true;
//		}
//		else {
//			return false;
//		}
		return false;
	}

	public void pickup(Person p) {
		System.out.println("Picked up passenger");
		passengers.add(p);
		p.setInWater(false);
		Collections.sort(passengers);
	}
}