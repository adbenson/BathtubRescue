package net.adbenson.android.bathtubrescue.render;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface AbstractModelRender {
	
	public void draw(Canvas g);
	
	public Rect getBounds();
	
	public boolean intersects(AbstractModelRender that);

}
