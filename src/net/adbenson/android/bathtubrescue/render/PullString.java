package net.adbenson.android.bathtubrescue.render;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

public class PullString implements DrawingQueueable, AbstractModelRender {
	
	public static final int LENGTH_MIN = 35;
	public static final int LENGTH_MAX = 300;
	public static final float BASE_WIDTH = 20;
	public static final float MIN_WIDTH = 0.5f;

	private static final float ELASTICITY = 0.01f;
	
	private static final float HALF_PI = (float) (Math.PI / 2.0);
	
	private Path topSpring;
	private Path bottomSpring;
	
	private Path tempPath;
		
	private Matrix scale;
	private Matrix rotate;
	
	private int color = Color.GREEN;
	
	private float width;
	
	private boolean held;
	
	private Paint paintTop;
	private Paint paintBottom;
	
	
	public PullString() {
		paintTop = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		paintTop.setColor(Color.GREEN);
		paintTop.setStyle(Paint.Style.STROKE); 
		paintTop.setStrokeWidth(3);
		
		paintBottom = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		paintBottom.setColor(Color.rgb(0, 192, 0));
		paintBottom.setStyle(Paint.Style.STROKE); 
		paintBottom.setStrokeWidth(3);
		
		scale = new Matrix();
		rotate = new Matrix();
		tempPath = new Path();
	}
	
	public void generateSprings() {
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

	public void enqueueForDraw(DrawingQueue queue) {
		queue.add(new Drawable(10) {
			@Override
			public void draw(Canvas g) {
//				g.setStroke(new BasicStroke(2f));
//				
//				Vector tempEnd = held? end : trail;
//				Vector path = start.subtract(tempEnd);
//				
//				AffineTransform scale = AffineTransform.getScaleInstance(path.magnitude() / LENGTH_MIN, width);
//				AffineTransform rotate = AffineTransform.getRotateInstance(path.getAngle()-HALF_PI);
//				AffineTransform translate = AffineTransform.getTranslateInstance(start.x, start.y);		
//				
//				Path tempTop = new Path(topSpring);
//				Path tempBottom = new Path(bottomSpring);
//				
//				tempTop.transform(scale);
//				tempBottom.transform(scale);
//				tempTop.transform(rotate);
//				tempBottom.transform(rotate);
//				tempTop.transform(translate);
//				tempBottom.transform(translate);
//				
//				g.setColor(color.darker().darker());
//				g.draw(tempBottom);
//				
//				g.setColor(color);
//				g.draw(tempTop);
				

//			    g.drawLine(start.intX(), start.intY(), tempEnd.intX(), tempEnd.intY());
				
				
			}
			
		});
	}

	public void draw(Canvas g) {
		// TODO Auto-generated method stub
		
	}

	public Rect getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean intersects(AbstractModelRender that) {
		// TODO Auto-generated method stub
		return false;
	}

	public void draw(Canvas g, Vector start, Vector end) {
		Vector path = start.subtract(end);
		
		scale.reset();
		scale.setScale(path.magnitude() / LENGTH_MIN, width);
		
		rotate.reset();
		scale.postRotate((float) Math.toDegrees(path.getAngle()-HALF_PI));
		
		tempPath.reset();
		bottomSpring.transform(scale, tempPath);
//		bottomSpring.transform(rotate, tempPath);
		g.drawPath(tempPath, paintBottom);
		
		tempPath.reset();
		topSpring.transform(scale, tempPath);
//		topSpring.transform(rotate, tempPath);
		g.drawPath(tempPath, paintTop);
		

		
//		g.drawLine(start.x, start.y, end.x, end.y, paintTop);
	}

	public void setWidth(float width) {
		this.width = width;
	}

}
