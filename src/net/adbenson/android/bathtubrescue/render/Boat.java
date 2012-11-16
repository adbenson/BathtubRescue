package net.adbenson.android.bathtubrescue.render;

import java.util.LinkedList;

import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;


public class Boat implements DrawingQueueable, AbstractModelRender{
			
	private Path prototype;
	private Region shape;
	
	private boolean crashed;
	
	private boolean debug = false;
	
	private PullString string;
	
	private LinkedList<Person> passengers;	
	
	private Paint paint;
	
	public Boat() {
		
		prototype = generateProtoShape();
//		prototype.transform(AffineTransform.getScaleInstance(1.5, 1.5));
		translateShape();

		paint = new Paint();
		paint.setColor(Color.YELLOW);
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
	
	public void translateShape() {
//		AffineTransform tx = new AffineTransform();
//		tx.translate(position.x, position.y);
//		tx.rotate(trajectory.getAngle());
//		shape = new Area(tx.createTransformedShape(prototype));
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

	public void draw(Canvas g) {
		g.drawRect(new Rect(0, 0, 50, 100), paint);
	}

	public boolean intersects(AbstractModelRender that) {
		// TODO Auto-generated method stub
		return false;
	}

	public void enqueueForDraw(DrawingQueue queue) {
		// TODO Auto-generated method stub
		
	}

}