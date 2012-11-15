package net.adbenson.android.bathtubrescue;

import android.util.Log;

public class Ticker implements Runnable {
	
	private static final String LOGTAG = Ticker.class.getCanonicalName();
	
	private int tick_ms = 20;
	private long lastTick;
	
	private volatile boolean running;
	private volatile boolean drawing;
	private volatile boolean paused;
	
	private final Timed timed;
	
	private volatile Thread thread;
	
	public Ticker(Timed timed, int tick_ms) {
		lastTick = 0;
		
		running = false;
		drawing = false;
		paused = false;
		
		this.timed = timed;
		this.tick_ms = tick_ms;
	}
	
	public synchronized void start() {
		Log.i(LOGTAG, "Ticker Started!");
		
		running = true;
		lastTick = System.currentTimeMillis();
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		while(running) {
			
			if (paused) {
				tryWait();
				lastTick = System.currentTimeMillis();
			}
			
			timed.tick();
			
			if (running && drawing) {
				timed.draw();
			}
			
			lastTick = System.currentTimeMillis();
			
			boolean waited = trySleep();
			if (!waited) {
				
			}
		}			
	}
	
	private synchronized boolean tryWait() {
		try {
			this.wait();
			return true;
		} catch (InterruptedException e) {
			Log.e(LOGTAG, "Ticker wait (pause) interrupted", e);
			return false;
		}
	}
	
	private boolean trySleep() {
		try {
			Thread.sleep(tick_ms);
			return true;
		} catch (InterruptedException e) {
			Log.e(LOGTAG, "Ticker sleep interrupted", e);
			return false;
		}
	}

	public synchronized void stop() {
		running = false;
	}
	
	public synchronized void setDrawing(boolean draw) {
		this.drawing = draw;
	}
	
	public void setTick_ms(int tick) {
		this.tick_ms = tick;
	}
	
	public synchronized void setPause(boolean pause) {
		this.paused = pause;
		
		if (!pause) {
			this.notify();
		}
	}

}
