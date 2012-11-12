package net.adbenson.android.bathtubrescue.objects;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;

public class PullString implements DrawingQueueable {
	
	public static final int LENGTH_MIN = 35;
	public static final int LENGTH_MAX = 300;
	public static final float BASE_WIDTH = 6;
	public static final float MIN_WIDTH = 0.5f;

	private static final float ELASTICITY = 0.01f;
	
	private static final float HALF_PI = (float) (Math.PI / 2.0);
	
	private Path topSpring;
	private Path bottomSpring;
	
	private Vector end;
	private Vector start;
	private Vector trail;
	
	private int color = Color.GREEN;
	
	private double width;
	
	private boolean held;
	
	public PullString() {
		start = new Vector(0, 0);
		end = new Vector(0, 0);
		generateSprings();
	}
	
	private void generateSprings() {
		float halfWidth = BASE_WIDTH / 2.0f;
		
		topSpring = new Path();
		bottomSpring = new Path();
		
		topSpring.moveTo(1, -halfWidth);
		bottomSpring.moveTo(2, halfWidth);
		
		for(int i=2; i<LENGTH_MIN - 4; i+=2) {
			topSpring.lineTo(i, halfWidth);
			topSpring.moveTo(i+1, -halfWidth);
			
			bottomSpring.lineTo(i+1, -halfWidth);
			bottomSpring.moveTo(i+2, halfWidth);
		}
	}
	
	public void drop() {
		System.out.println("String dropped");
		held = false;
		width = 1;
	}
	
	public void grab() {
		System.out.println("String grabbed");
		held = true;
	}

	public boolean isHeld() {
		return held;
	}
	
	public double pull(double distance) {
		
		if (distance > LENGTH_MAX) {
			drop();
		}
		
		setWidth(distance);
		
		if (held) {
			return (distance - LENGTH_MIN) * ELASTICITY;
		}
		else {
			return 0;
		}

	}
	
	private void setWidth(double distance) {
		if (held && distance > LENGTH_MIN) {
			double distanceRatio = ((distance - LENGTH_MIN) / (LENGTH_MAX - LENGTH_MIN));
			width = 1 - distanceRatio;
		}
		else {
			width = 1;
		}
	}
	
	public void setStart(Vector start) {
		this.start = start;
	}
	
	public void setEnd(Vector end) {
		this.end = end;		
	}

	public void setTrail(Vector trajectory) {
		trail = start.add(trajectory.normalize().invert().scale(PullString.LENGTH_MIN*2));
	}

	public Vector getEnd() {
		return end;
	}

	public void enqueueForDraw(DrawingQueue queue) {
		queue.add(new Drawable(10) {
			@Override
			public void draw(Canvas g) {
				g.setStroke(new BasicStroke(2f));
				
				Vector tempEnd = held? end : trail;
				Vector path = start.subtract(tempEnd);
				
				AffineTransform scale = AffineTransform.getScaleInstance(path.magnitude() / LENGTH_MIN, width);
				AffineTransform rotate = AffineTransform.getRotateInstance(path.getAngle()-HALF_PI);
				AffineTransform translate = AffineTransform.getTranslateInstance(start.x, start.y);
				
				Path tempTop = new Path2D.Double(topSpring);
				Path tempBottom = new Path2D.Double(bottomSpring);
				
				tempTop.transform(scale);
				tempBottom.transform(scale);
				tempTop.transform(rotate);
				tempBottom.transform(rotate);
				tempTop.transform(translate);
				tempBottom.transform(translate);
				
				g.setColor(color.darker().darker());
				g.draw(tempBottom);
				
				g.setColor(color);
				g.draw(tempTop);
				

//			    g.drawLine(start.intX(), start.intY(), tempEnd.intX(), tempEnd.intY());
				
				
			}
			
		});
	}

	public Vector getTrail() {
		return trail;
	}

}
