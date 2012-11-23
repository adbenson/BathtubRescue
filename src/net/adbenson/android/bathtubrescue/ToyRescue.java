package net.adbenson.android.bathtubrescue;

import java.util.LinkedList;
import java.util.Random;

import net.adbenson.android.bathtubrescue.model.Boat;
import net.adbenson.android.bathtubrescue.model.Handle;
import net.adbenson.android.bathtubrescue.model.Person;
import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.QueuePopulator;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

public class ToyRescue implements Timed, QueuePopulator, InputReciever{
	
	private static final String LOGTAG = ToyRescue.class.getCanonicalName();

	public enum State {
		START (false, true, false),
		DROPPED (true, true, false),
		GRABBED (true, false, false),
		CLEARED (true, false, false),
		WON (false, false, true),
		LOST (false, false, true),
		PAUSE (false, false, false);
		
		public final boolean isRunning;
		public final boolean canPickup;
		public final boolean gameOver;
		
		State(boolean isRunning, boolean canPickup, boolean gameOver) {
			this.isRunning = isRunning;
			this.canPickup = canPickup;
			this.gameOver = gameOver;
		}
	}
	
	private CustomSurface drawingSurface;
	
	private Ticker ticker;
	
	private State state;
	private State resumeState;
	
	private Boat boat;
	
	private Handle handle;
	
	private LinkedList<Person> survivors;
	private LinkedList<Person> floating;
	
	public ToyRescue(CustomSurface surface) {
		Log.i(LOGTAG, "Initializing ToyRescue");
		
		this.drawingSurface = surface;
		
		state = State.START;
		
		boat = new Boat(100, 100);

		initializePeople();
		
		handle = new Handle(50, 50);
		
		ticker = new Ticker(this, 20);
	}

	private void initializePeople() {
		survivors = new LinkedList<Person>();
		floating = new LinkedList<Person>();
		
		Random r = new Random(System.currentTimeMillis());
		for(int i=0; i < 10; i++) {
			Vector location = new Vector(r.nextInt(760)+20, r.nextInt(540)+40);
			Person p = new Person(Color.HSVToColor(new float[] {r.nextFloat(), 1.0f, 1.0f}), location, (r.nextDouble()*0.25)+0.25);
			survivors.add(p);
			floating.add(p);
		}
	}

	public void grabbed(Vector location) {
		Log.v(LOGTAG, "Touch down");

		if (state.canPickup && handle.contains(location)) {
			Log.d(LOGTAG, "Handle Grabbed");
			state = State.GRABBED;
		}
	}

	public void dragged(Vector location) {
		Log.v(LOGTAG, "Touch dragged");
	
		if (state == State.GRABBED) {
			handle.setLocation(location);
		}
	}

	public void dropped(Vector location) {
		Log.v(LOGTAG, "Touch released");
		
		if (state == State.GRABBED) {
			Log.d(LOGTAG, "Handle Grabbed");
			state = State.DROPPED;
		}
	}
	
	public void draw() {
        drawingSurface.drawFrom(this);
	}
	
	public void populateQueue(DrawingQueue queue) {
		queue.add(new DrawingQueueable() {

			public void enqueueForDraw(DrawingQueue queue) {
				queue.add(new Drawable(-100) {

					@Override
					public void draw(Canvas g) {
						g.drawColor(Color.rgb(168, 221, 237));
					}
										
				});
			}
			
		});
		
        queue.add(boat);

//       	queue.add(floating);
        
        queue.add(handle);
	}
		
	public void tick() {
//		handle.set(handle.x + 1, handle.y + 1);
		if (state.isRunning) {
			boat.pull(handle);
			boat.move();
			Log.v(LOGTAG, "Tick");

			detectCollisions();
		}
	}
		
	private void detectCollisions() {
		if (state.isRunning){ 
//			Rectangle toyBounds = toy.getBounds();
//			
//			boolean wallCrash = ! window.getWall().contains(toyBounds);
//			boolean handleCrash = handle.intersects(toy.getShape());
//			if (wallCrash || handleCrash) { 
//				System.out.println("Crash: wall?"+wallCrash+" handle?"+handleCrash);
//				toy.crash();
//				state = State.LOST;
//			}
//			
//			LinkedList<Person> pickedUp = new LinkedList<Person>();
//			for(Person p : floating) {
//				if (toy.intersects(p)) {
//					toy.pickup(p);
//					pickedUp.add(p);
//				}
//			}
//			floating.removeAll(pickedUp);
//			
//			if (floating.isEmpty()) {
//				
//			}
		}
		
	}
	
	public void start() {
        ticker.setDrawing(true);
        ticker.start();
	}
	
	public void boardCleared() {
		state = State.CLEARED;
	}
	
	public void gameLost() {
		state = State.LOST;
	}
	
	public void gameWon() {
		state = State.WON;
	}
	
	public void setPause(boolean pause) {
		if(pause && state.isRunning) {
			ticker.setPause(true);
			resumeState = state;
			state = State.PAUSE;
		}
		else if (!pause && state == State.PAUSE) {
			ticker.setPause(false);
			state = resumeState;
		}
	}

	public CustomSurface getDrawingSurface() {
		return drawingSurface;
	}

	public void setDrawingSurface(CustomSurface drawingSurface) {
		this.drawingSurface = drawingSurface;
	}

}