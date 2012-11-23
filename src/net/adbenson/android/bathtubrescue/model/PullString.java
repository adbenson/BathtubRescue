package net.adbenson.android.bathtubrescue.model;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class PullString implements DrawingQueueable {
	
	private static final String LOGTAG = PullString.class.getCanonicalName();
		
	public static final int LENGTH_MIN = 35;
	public static final int LENGTH_MAX = 300;
	public static final float BASE_WIDTH = 6;
	public static final float MIN_WIDTH = 0.5f;

	private static final float ELASTICITY = 0.01f;
	
	private static final float HALF_PI = (float) (Math.PI / 2.0);
	
	private net.adbenson.android.bathtubrescue.render.PullString render;
	
	private Vector end;
	private Vector start;
	private Vector trail;
	
	private boolean held;
	
	public PullString() {
		start = new Vector(0, 0);
		end = new Vector(0, 0);
		
		render = new net.adbenson.android.bathtubrescue.render.PullString();
		render.generateSprings();
	}
	
	public void drop() {
		System.out.println("String dropped");
		held = false;
		render.setWidth(1);
	}
	
	public void grab() {
		System.out.println("String grabbed");
		held = true;
	}

	public boolean isHeld() {
		return held;
	}
	
	public float pull(float distance) {
		
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
	
	private void setWidth(float distance) {
		if (held && distance > LENGTH_MIN) {
			float distanceRatio = ((distance - LENGTH_MIN) / (LENGTH_MAX - LENGTH_MIN));
			render.setWidth(1 - distanceRatio);
		}
		else {
			render.setWidth(1);
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
				Vector tempEnd = held? end : trail;
				g.translate(start.x, start.y);
				
				render.draw(g, start, tempEnd);

			}
			
		});
	}

	public Vector getTrail() {
		return trail;
	}

}
