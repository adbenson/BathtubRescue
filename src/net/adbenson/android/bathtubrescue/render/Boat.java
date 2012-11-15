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
			
	private Path prototype;
	private Region shape;
	
	private boolean crashed;
	
	private boolean debug = false;
	
	private PullString string;
	
	private LinkedList<Person> passengers;	
	
	public Boat() {
		
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
	
	public void translateShape() {
//		AffineTransform tx = new AffineTransform();
//		tx.translate(position.x, position.y);
//		tx.rotate(trajectory.getAngle());
//		shape = new Area(tx.createTransformedShape(prototype));
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

}