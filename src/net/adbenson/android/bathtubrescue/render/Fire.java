package net.adbenson.android.bathtubrescue.render;

import net.adbenson.android.drawing.BoundVector;
import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Fire implements DrawingQueueable, AbstractModelRender {

	private Vector outerBaseLeft = new Vector(-10, 0);
	private Vector outerBaseRight = new Vector(10, 0);
	
	private BoundVector outerCP1Left = new BoundVector(-25, -18, -15, -10);
	private BoundVector outerCP2Left = new BoundVector(-10, -30, 0, -22);
	private BoundVector outerCPRight = new BoundVector(15, -25, 25, -15);
	
	private BoundVector outerPeak = new BoundVector(-10, -40, 0, -30);
	
	private Vector innerBaseLeft = new Vector(-5, 0);
	private Vector innerBaseRight = new Vector(5, 0);
	
	private BoundVector innerCPLeft = new BoundVector(-10, 0, -10, 0);
	private BoundVector innerCPRight = new BoundVector(10, 0, 10, 0);
	
	private BoundVector innerPeak = new BoundVector(-5, -15, 5, -20);
	
	private BoundVector[] boundVectors = {outerCP1Left, outerCP2Left, outerCPRight, outerPeak, innerCPLeft, innerCPRight, innerPeak};
	
	private Vector position;
	
	public void setPosition(Vector position) {
		this.position = position;
	}

	public void enqueueForDraw(DrawingQueue queue) {
		queue.add(new Drawable(50) {
			@Override
			public void draw(Canvas g) {
//				Random rand = new Random(System.currentTimeMillis());
//				int i = rand.nextInt(boundVectors.length);
//				boundVectors[i].randomize();
//				
//				outerPeak.randomize();
//				
//				g.setTransform(AffineTransform.getTranslateInstance(position.intX(), position.intY()));
////				g.setTransform(AffineTransform.getScaleInstance(2, 2));
//				
//				GeneralPath outer = new GeneralPath();
//				outer.moveTo(outerBaseLeft.x, outerBaseLeft.y);
//				outer.curveTo(outerCP1Left.x, outerCP1Left.y, outerCP2Left.x, outerCP2Left.y, outerPeak.x, outerPeak.y);
//				outer.quadTo(outerCPRight.x, outerCPRight.y, outerBaseRight.x, outerBaseRight.y);
////				outer.closePath();
//				
//				g.setColor(Color.red);
//				g.fill(outer);
//				
//				GeneralPath inner = new GeneralPath();
//				inner.moveTo(innerBaseLeft.x, innerBaseLeft.y);
//				inner.quadTo(innerCPLeft.x, innerCPRight.y, outerPeak.x+5, innerPeak.y);
//				inner.quadTo(innerCPRight.x, innerCPRight.y, innerBaseRight.x, innerBaseRight.y);
//				inner.closePath();
//				
//				g.setColor(Color.yellow);
//				g.fill(inner);
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


}
