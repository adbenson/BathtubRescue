package net.adbenson.android.bathtubrescue.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Wall implements AbstractModelRender {
	
	public static final int WALL_WIDTH = 20;
	public static final int MENU_HEIGHT = 20;
	
	private int windowHeight;
	private int windowWidth;
	
	private Path wall; 
	private Paint paint;
	
	public Wall() {
		Paint = new Paint
	}

	public void draw(Canvas g) {
		
		if (wall != null) {
			g.drawPath(wall, paint);
		}
	}
	
	protected Path generateWall() {
		Path wall = new Path();
		int width = windowWidth;
		int height = windowHeight;
		int ww = WALL_WIDTH;
		int wm = ww + MENU_HEIGHT;
		
		//Inner border
		wall.moveTo(ww, wm);
		wall.lineTo(width - ww, wm);
		wall.lineTo(width - ww, height - ww);
		wall.lineTo(ww, height - ww);
		wall.lineTo(ww, wm);
		wall.close();
		
		return wall;
	}
	public void setWindowDimensions(int width, int height) {
		this.windowWidth = width;
		this.windowHeight = height;
	}

}
