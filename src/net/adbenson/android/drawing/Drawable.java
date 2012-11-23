package net.adbenson.android.drawing;

import java.util.Comparator;

import android.graphics.Canvas;

public abstract class Drawable {
	
	private int layer;
	
	public Drawable() {
		this(0);
	}
	
	public Drawable(int layer) {
		this.layer = layer;
	}

	protected final void triggerDraw(Canvas g) {
		setUp(g);
		
		draw(g);
		
		tearDown(g);
	}

	public abstract void draw(Canvas g);

	public final int getDrawLayer() {
		return layer;
	}
	
	public final void setDrawLayer(int layer) {
		this.layer = layer;
	}
	
	protected void setUp(Canvas g) {
		g.save();
	}
	
	protected void tearDown(Canvas g) {
		g.restore();
	}
	
	public static final class Compare implements Comparator<Drawable> {
		public int compare(Drawable arg0, Drawable arg1) {
			return arg0.layer - arg1.layer;
		}
	}
}
