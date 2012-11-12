package net.adbenson.android.bathtubrescue.objects;

import java.util.Random;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.hardware.Camera.Area;


public class Person implements Comparable, DrawingQueueable{
	private static final Area PROTO = generatePrototype();
	private static final Path WAVE = generateWaveShape();
	private static final Path WAVE_CREST = generateWaveCrest();
	
	public static final double swayRate = 0.1;
	
	private Area shape;
	private Path wave;
	private Path waveCrest;
	private Color color;
	private Vector location;
	private double size;
	
	private double sway;
	private double swaySpeed;
	private double swayOffset;
	
	private boolean inWater;
	
	public Person(Color color, Vector location, double scale) {
		shape = (Area) PROTO.clone();
		wave = (Path) WAVE.clone();
		waveCrest = (Path) WAVE_CREST.clone();
		
		AffineTransform scaler = AffineTransform.getScaleInstance(scale, scale);
		
		shape.transform(scaler);
		wave.transform(scaler);
		waveCrest.transform(scaler);
		
		this.color = color;
		this.location = location;
		this.size = scale;
		this.swayOffset = new Random().nextDouble();
		this.swaySpeed = (new Random().nextDouble() / 4);
		
		inWater = true;
	}
	
	private void updateSway() {
		swayOffset = Math.sin(sway += swaySpeed) * 3;
	}
	
	private static Path generateWaveShape() {
		Path wave = new Path();
		wave.moveTo(-15, 0);
		wave.cubicTo(-15, 10, -5, 10, -5, 0);
		wave.cubicTo(-5, 10, 5, 10, 5, 0);
		wave.cubicTo(5, 10, 15, 10, 15, 0);
		wave.lineTo(15, 35);
		wave.lineTo(-15, 35);
		wave.close();
		
		return wave;
	}
	
	private static Path generateWaveCrest() {
		Path wave = new Path();
		wave.moveTo(-15, 0);
		wave.cubicTo(-15, 10, -5, 10, -5, 0);
		wave.cubicTo(-5, 10, 5, 10, 5, 0);
		wave.cubicTo(5, 10, 15, 10, 15, 0);
		
		return wave;
	}
	
	private static Area generatePrototype() {
		Path body = new Path();
		body.moveTo(1, 30);//between feet
		body.lineTo(10, 30);//R foot
		body.lineTo(10, 0);//R armpit
		body.lineTo(25, -15);//R hand
		body.lineTo(20, -20);//R hand
		body.lineTo(10, -10);//R shoulder
		body.lineTo(-10, -10);//L shoulder
		body.lineTo(-20, -20);//L hand
		body.lineTo(-25, -15);//L hand
		body.lineTo(-10, 0);//L armpit
		body.lineTo(-10, 30);//L foot
		body.lineTo(-1, 30);//between feet
		body.lineTo(-1, 10);//crotch
		body.lineTo(1, 10);//crotch
		body.close();
		
		Ellipse head = new Ellipse2D.Double(-10, -30, 20, 20);
		
		Area area = new Area(body);
		area.add(new Area(head));
		return area;
	}

	public Vector getLocation() {
		return location;
	}
	
	public void setLocation(Vector location) {
		this.location = location;
	}
	
	public void setInWater(boolean inWater) {
		this.inWater = inWater;
	}
	
	public void enqueueForDraw(DrawingQueue queue) {
		queue.add(new Drawable(5) {
			public void draw(Canvas g) {
				
				Vector tempLocation = location;
				
				if (inWater) {
					updateSway();
					tempLocation = tempLocation.add(0, swayOffset);
				}
				
				g.setTransform(AffineTransform.getTranslateInstance(tempLocation.x, tempLocation.y));
						
				g.setStroke(new BasicStroke(1));
				g.setColor(color);
				g.fill(shape);
				g.setColor(Color.black);
				g.draw(shape);
				
				if (inWater) {
					g.setColor(Color.blue);
//					g.setTransform(AffineTransform.getTranslateInstance(location.x, location.y));
					g.fill(wave);
					g.setColor(Color.black);
					g.draw(waveCrest);
				}
			}
		});
	}

	public int compareTo(Object arg0) {
		return (int) Math.signum(((Person)arg0).size - this.size);
	}
	
	public Rectangle getBounds() {
		Rectangle bounds = shape.getBounds();
		bounds.translate(location.intX(), location.intY());
		return bounds;
	}
}