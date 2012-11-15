package net.adbenson.android.bathtubrescue.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.SurfaceHolder;

public class Handle implements AbstractModelRender{
	
	private static final String LOGTAG = Handle.class.getCanonicalName();
	
	public static final int SIZE = 40;
	public static final int HALF_SIZE = SIZE / 2;
	
	private RectF oval;
	private Paint paint;
	
	public Handle() {
		oval = new RectF(0, 0, SIZE, SIZE);
		paint = new Paint();
		paint.setColor(Color.rgb(255, 0, 0));
	}

//	public boolean contains(Point point) {
//		return (point.length(this) <= halfSize);
//	}
//
//	public boolean contains(Vector point) {
//		return (point.length(this) <= halfSize);
//	}
	
	public Rect getBounds() {
		return new Rect(0, 0, SIZE, SIZE);
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

	public void draw(Canvas g) {
		g.drawOval(oval, paint);
	}

	public boolean intersects(AbstractModelRender that) {
		// TODO Auto-generated method stub
		return false;
	}

}
