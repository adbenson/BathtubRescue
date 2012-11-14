package net.adbenson.android.bathtubrescue.model;

import java.util.Random;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Camera.Area;


public class Person implements Comparable, DrawingQueueable{
	
	public static final double swayRate = 0.1;

	private net.adbenson.android.bathtubrescue.render.Person personRender;
	private net.adbenson.android.bathtubrescue.render.Wave waveRender;

	private Vector location;
	private double size;
	
	private double sway;
	private double swaySpeed;
	private double swayOffset;
	
	private boolean inWater;
	
	public Person(int color, Vector location, double scale) {
		
		this.location = location;
		this.size = scale;
		this.swayOffset = new Random().nextDouble();
		this.swaySpeed = (new Random().nextDouble() / 4);
		
		inWater = true;
	}
	
	private void updateSway() {
		swayOffset = Math.sin(sway += swaySpeed) * 3;
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
				
				personRender.setLocation(tempLocation);
				personRender.draw(g);
				
				if (inWater) {
					waveRender.setLocation(tempLocation);
					waveRender.draw(g);
				}
			}
		});
	}

	public int compareTo(Object arg0) {
		return (int) Math.signum(((Person)arg0).size - this.size);
	}

}
