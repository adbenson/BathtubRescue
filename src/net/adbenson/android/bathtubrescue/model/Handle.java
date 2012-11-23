package net.adbenson.android.bathtubrescue.model;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.SurfaceHolder;

public class Handle extends Vector implements DrawingQueueable{
	
	private static final String LOGTAG = Handle.class.getCanonicalName();
	
	private net.adbenson.android.bathtubrescue.render.Handle render;

	public Handle(int i, int j) {
		super(i, j);
		
		render = new net.adbenson.android.bathtubrescue.render.Handle();
	}
	
	public void setLocation(Vector location) {
		this.x = location.x;
		this.y = location.y;
	}

//	public boolean contains(Point point) {
//		return (point.length(this) <= halfSize);
//	}

	public boolean contains(Vector point) {
		return (point.length(this) <= net.adbenson.android.bathtubrescue.render.Handle.HALF_SIZE);
	}
	
	public Rect getBounds() {
		Rect bounds = render.getBounds();
		bounds.offsetTo(intX(), intY());
		return bounds;
	}

	public boolean intersects(Shape shape) {
//		Rectangle shapeBounds = shape.getBounds();
//		if (! shapeBounds.intersects(this.getBounds())) {
//			return false;
//		}
//		
//		Point2D.Double shapeCenter = new Point2D.Double(shapeBounds.getCenterX(), shapeBounds.getCenterY());
//		if (shapeCenter.distance(this) <= halfSize) {
//			System.out.println("Handle collision shortcut failed");
//			return true;
//		}
//		
//		//If the bounding boxes intersect but the center is not inside the circle, we need to do more careful testing.
//		System.out.println("Handle collision detail testing started");
//		
//		PathIterator pi = shape.getPathIterator(null);
//		FlatteningPathIterator flattened = new FlatteningPathIterator(pi, 3, 3);
//		double[] coords = new double[6];
//		while(! flattened.isDone()) {
//			int segType = flattened.currentSegment(coords);
//			if (this.distance(new Point2D.Double(coords[0], coords[1])) <= halfSize) {
//				System.out.println("Handle crash");
//				return true;
//			}
//			flattened.next();
//		}
		return false;
	}

	public void enqueueForDraw(DrawingQueue queue) {
		queue.add(new Drawable(15) {
			public void draw(Canvas g) {
//				Log.v(LOGTAG, x+", "+y);
				g.translate(x - render.HALF_SIZE, y - render.HALF_SIZE);
		        render.draw(g);
			}
		});
		
	}
}
