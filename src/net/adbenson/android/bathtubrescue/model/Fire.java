package net.adbenson.android.bathtubrescue.model;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;

public class Fire implements DrawingQueueable {
	
	private net.adbenson.android.bathtubrescue.render.Fire render;
	
	private Vector position;
	
	public void setPosition(Vector position) {
		this.position = position;
	}

	public void enqueueForDraw(DrawingQueue queue) {
		queue.add(new Drawable(50) {
			@Override
			public void draw(Canvas g) {
				
				render.setPosition(position);
				render.draw(g);
			}
		});
	}


}
