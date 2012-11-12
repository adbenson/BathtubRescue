package net.adbenson.android.bathtubrescue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CustomSurface extends SurfaceView implements SurfaceHolder.Callback, Timed {
	
	private static final String TAG = SurfaceHolder.class.getCanonicalName();
	
	private volatile SurfaceHolder currentHolder;
	
	private Ticker ticker;
	
	private volatile int width;
	private volatile int height;
	private volatile int format;
	
	private volatile int x;
	private volatile int y;
	
	public CustomSurface(Context context, AttributeSet attrs) {
		
		super(context);
		
		currentHolder = getHolder();
		currentHolder.addCallback(this);
		
		ticker = new Ticker(this, 20);
		
		x = 1;
		y = 1;
		
//		this.setOnDragListener(new View.OnDragListener() {
//
//			public boolean onDrag(View v, DragEvent event) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//		});
		
		this.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				x = (int) event.getX();
				y = (int) event.getY();
				
				return true;
			}
			
		});
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		ticker.setDrawing(false);
		
		currentHolder = holder;
		
		this.format = format;
		this.width = width;
		this.height = height;
		
		this.x = width / 2;
		this.y = height / 2;
		
		//Adjust parameters
		
		ticker.setDrawing(true);		
	}
	public void surfaceCreated(SurfaceHolder holder) {
		currentHolder = holder;
		ticker.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		currentHolder = holder;
		ticker.setDrawing(false);
	}

	public void tick() {
		x = ++x % width;
	}

	public void draw() {
		Canvas canvas = currentHolder.lockCanvas();
		
		canvas.drawColor(Color.CYAN);
		
		Paint p = new Paint();
		p.setColor(Color.RED);
		Log.v(TAG, "H:"+height);
		Log.v(TAG, "H/2:"+height / 2);
		canvas.drawCircle(x, y, 60, p);
		
		currentHolder.unlockCanvasAndPost(canvas);
	}

}
