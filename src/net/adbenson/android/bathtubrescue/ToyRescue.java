package net.adbenson.android.bathtubrescue;

import java.util.LinkedList;
import java.util.Random;

import net.adbenson.android.bathtubrescue.model.Boat;
import net.adbenson.android.bathtubrescue.model.Handle;
import net.adbenson.android.bathtubrescue.model.Person;
import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.Vector;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Camera.Area;

public class ToyRescue {
	
	public static void main(String[] args) {
		new ToyRescue();
	}
	
	public enum State {
		START (false, true, false),
		DROPPED (true, true, false),
		GRABBED (true, false, false),
		CLEARED (true, false, false),
		WON (false, false, true),
		LOST (false, false, true);
		
		public final boolean isRunning;
		public final boolean canPickup;
		public final boolean gameOver;
		
		State(boolean isRunning, boolean canPickup, boolean gameOver) {
			this.isRunning = isRunning;
			this.canPickup = canPickup;
			this.gameOver = gameOver;
		}
	}
	
	private State state;
		
	private Window window;
	
	private Painter painter;
	
	private Boat toy;
	
	private Handle handle;
	
	private LinkedList<Person> survivors;
	private LinkedList<Person> floating;
	
	
	public ToyRescue() {
		state = State.START;
		
		window = new Window();
		toy = new Boat(400, 300);
		painter = new Painter();
		survivors = new LinkedList<Person>();
		floating = new LinkedList<Person>();
		Random r = new Random(System.currentTimeMillis());
		for(int i=0; i < 10; i++) {
			Vector location = new Vector(r.nextInt(760)+20, r.nextInt(540)+40);
			Person p = new Person(Color.HSVToColor(new float[] {r.nextFloat(), 1.0f, 1.0f}), location, (r.nextDouble()*0.25)+0.25);
			survivors.add(p);
			floating.add(p);
		}
		
		window.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent mouse) {
				if (state == GRABBED) {
					handle.setLocation(mouse.getPoint());
				}
			}
		});
		
		window.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				mouseDown(arg0);
			}
			public void mouseReleased(MouseEvent arg0) {
				mouseUp(arg0);
			}
			
		});
		
		handle = new Handle(500, 200);
		
		painter.start();
	}
	
	private void mouseDown(MouseEvent mouse) {
		if (state.canPickup && handle.contains(mouse.getPoint())) {
			state = State.GRABBED;
			window.showCursor(false);
		}
	}
	
	private void mouseUp(MouseEvent mouse) {
		window.showCursor(true);
		if (state == State.GRABBED) {
			state = State.DROPPED;
		}
	}
	
	class Window extends JFrame {
		
		private Area wall;
		
		public static final int WALL_WIDTH = 20;
		public static final int MENU_HEIGHT = 20;
		
		private final Cursor blankCursor;
		
		public Window() {
	        this.getRootPane().addComponentListener(new ComponentAdapter() {
	            public void componentResized(ComponentEvent e) {
	                wall = generateWall();
	            }
	        });
			
			wall = generateWall();
			
			// Transparent 16 x 16 pixel cursor image.
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			// Create a new blank cursor.
			blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(0, 0), "blank cursor");
		}
		
		public void showCursor(boolean show) {
			if (show) {
				getContentPane().setCursor(null);
			}
			else {
				getContentPane().setCursor(blankCursor);
			}
		}
		
		protected Area generateWall() {
			GeneralPath wall = new GeneralPath();
			int width = this.getWidth();
			int height = this.getHeight();
			int ww = WALL_WIDTH;
			int wm = ww + MENU_HEIGHT;
			
			//Inner border
			wall.moveTo(ww, wm);
			wall.lineTo(width - ww, wm);
			wall.lineTo(width - ww, height - ww);
			wall.lineTo(ww, height - ww);
			wall.lineTo(ww, wm);
			wall.closePath();
			
			return new Area(wall);
		}
		
		public Shape getWall() {
			return wall;
		}
				
		public void paint(Graphics g) {
			Image offscreen = createImage(this.getWidth(), this.getHeight());
			
			Graphics2D g2 = (Graphics2D) offscreen.getGraphics();			
			
	        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
	                              RenderingHints.VALUE_ANTIALIAS_ON );
	        g2.setRenderingHint( RenderingHints.KEY_RENDERING,
	                              RenderingHints.VALUE_RENDER_QUALITY );
	        
	        DrawingQueue queue = new DrawingQueue(g2);
	        
//	        g.setColor(Color.orange);
//	        g.fillRect(0, 0, this.getWidth(), this.getHeight());
	        
	        queue.add(new Drawable(-50) {
				@Override
				public void draw(Graphics2D g) {
			        g.setColor(Color.blue);
			        g.fill(wall);
				}
	        });

	        queue.add(toy);

	       	queue.add(floating);
	        
	        queue.add(handle);
	        
	        queue.draw();
	        g.drawImage(offscreen, 0, 0, this);
		}

	}
		
	protected void tick() {
		if (state.isRunning) {
			toy.pull(handle);
			toy.move();
			detectCollisions();
		}
	}
		
	private void detectCollisions() {
		if (state.isRunning){ 
			Rectangle toyBounds = toy.getBounds();
			
			boolean wallCrash = ! window.getWall().contains(toyBounds);
			boolean handleCrash = handle.intersects(toy.getShape());
			if (wallCrash || handleCrash) { 
				System.out.println("Crash: wall?"+wallCrash+" handle?"+handleCrash);
				toy.crash();
				state = LOST;
			}
			
			LinkedList<Person> pickedUp = new LinkedList<Person>();
			for(Person p : floating) {
				if (toy.intersects(p)) {
					toy.pickup(p);
					pickedUp.add(p);
				}
			}
			floating.removeAll(pickedUp);
			
			if (floating.isEmpty()) {
				
			}
		}
		
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

	class Painter {
		private static final int TICK_ms = 20;
		private long lastTick = 0;
		private volatile boolean run;
		
		public synchronized void start() {
			run = true;
			loop();
		}
		
		private void loop() {
			while(run) {
				tick();
				window.repaint();
				try {
					Thread.sleep(TICK_ms);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
		
		public synchronized void stop() {
			run = false;
		}
		
	}

}