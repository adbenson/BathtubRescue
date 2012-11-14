package net.adbenson.android.bathtubrescue.model;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.shapes.Shape;

public class Handle extends Vector implements DrawingQueueable{
	
	private net.adbenson.android.bathtubrescue.render.Handle render;

	public Handle(int i, int j) {
		super(i, j);
	}

//	public boolean contains(Point point) {
//		return (point.length(this) <= halfSize);
//	}

	public boolean contains(Vector point) {
		return (point.length(this) <= halfSize);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(intX()-halfSize, intY()-halfSize, SIZE, SIZE);
	}

	public boolean intersects(Shape shape) {
		Rectangle shapeBounds = shape.getBounds();
		if (! shapeBounds.intersects(this.getBounds())) {
			return false;
		}
		
		Point2D.Double shapeCenter = new Point2D.Double(shapeBounds.getCenterX(), shapeBounds.getCenterY());
		if (shapeCenter.distance(this) <= halfSize) {
			System.out.println("Handle collision shortcut failed");
			return true;
		}
		
		//If the bounding boxes intersect but the center is not inside the circle, we need to do more careful testing.
		System.out.println("Handle collision detail testing started");
		
		PathIterator pi = shape.getPathIterator(null);
		FlatteningPathIterator flattened = new FlatteningPathIterator(pi, 3, 3);
		double[] coords = new double[6];
		while(! flattened.isDone()) {
			int segType = flattened.currentSegment(coords);
			if (this.distance(new Point2D.Double(coords[0], coords[1])) <= halfSize) {
				System.out.println("Handle crash");
				return true;
			}
			flattened.next();
		}
		return false;
	}

	public void enqueueForDraw(DrawingQueue queue) {
		queue.add(new Drawable(15) {
			public void draw(Canvas g) {
		        render.draw(g);
			}
		});
		
	}

}
