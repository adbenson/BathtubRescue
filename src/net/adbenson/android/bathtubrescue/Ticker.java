package net.adbenson.android.bathtubrescue;

public class Ticker implements Runnable {
	
	private int tick_ms = 20;
	private long lastTick;
	private volatile boolean running;
	private volatile boolean drawing;
	private final Timed timed;
	
	public Ticker(Timed timed, int tick_ms) {
		lastTick = 0;
		
		running = false;
		drawing = false;
		
		this.timed = timed;
		this.tick_ms = tick_ms;
	}
	
	public synchronized void start() {
		running = true;
		lastTick = System.currentTimeMillis();
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		while(running) {
			
			timed.tick();
			
			if (running && drawing) {
				timed.draw();
			}
			
			lastTick = System.currentTimeMillis();
			
			boolean waited = tryWait();
			if (!waited) {
				
			}
		}			
	}
	
	private boolean tryWait() {
		try {
			Thread.sleep(tick_ms);
			return true;
		} catch (InterruptedException e) {
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

}
