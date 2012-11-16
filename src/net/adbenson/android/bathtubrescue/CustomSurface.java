package net.adbenson.android.bathtubrescue;

import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.QueuePopulator;
import net.adbenson.android.drawing.Vector;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CustomSurface extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
		
	private static final String LOGTAG = SurfaceHolder.class.getCanonicalName();
	
	private volatile SurfaceHolder currentHolder;
	
//	private Ticker ticker;
	
	private InputReciever reciever;
	
	public CustomSurface(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		currentHolder = getHolder();
		currentHolder.addCallback(this);
		
		this.setOnTouchListener(this);
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		if (reciever == null) {
			return false;
		}
		
		Vector location = new Vector(event.getX(), event.getY());
		
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			reciever.grabbed(location);
			break;
		case MotionEvent.ACTION_MOVE:
			reciever.dragged(location);
			break;
		case MotionEvent.ACTION_UP:
			reciever.dropped(location);
			break;
		default:
			Log.i(LOGTAG, "Unrecognized MotionEvent:"+event.getActionMasked());
		}
		
		return true;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		reciever.setPause(true);
		
		currentHolder = holder;
				
		reciever.setPause(false);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		currentHolder = holder;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		currentHolder = holder;
		reciever.setPause(true);
	}

	public void drawFrom(QueuePopulator populator) {
		Canvas canvas = currentHolder.lockCanvas();

		if (canvas == null) {
			Log.d(LOGTAG, "Draw requested but canvas not ready");
			return;
		}
		
		DrawingQueue queue = new DrawingQueue(canvas);
		
		populator.populateQueue(queue);
		
		queue.draw();
		
		currentHolder.unlockCanvasAndPost(canvas);
	}
	
	public void setInputReciever(InputReciever reciever) {
		this.reciever = reciever;
	}

}
