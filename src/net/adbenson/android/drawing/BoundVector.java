package net.adbenson.android.drawing;

import java.util.Random;

public class BoundVector extends Vector {
	
	float lowX;
	float lowY;
	float highX; 
	float highY;
	
	float diffX;
	float diffY;
	
	Random rand;
	
	public BoundVector(float lowX, float lowY, float highX,
			float highY) {
		this.lowX = lowX;
		this.lowY = lowY;
		this.highX = highX;
		this.highY = highY;
		
		this.x = (lowX + highX) / 2f;
		this.y = (lowY + highY) / 2f;
		
		diffX = highX - lowX;
		diffY = highY - lowY;
		
		rand = new Random(System.currentTimeMillis());
	}
	
	public void randomize() {
		x = randomize(x, diffX, lowX, highX);
		y = randomize(y, diffY, lowY, highY);
	}
	
	private float randomize(float val, float diff, float low, float high) {
		if (diff > 0) {
			float randFactor = (rand.nextFloat() - 0.5f) / 3f;		
			float delta = diff * randFactor;
			return constrain(val + delta, low, high);
		}
		else {
			return val;
		}
	}
	
	private float constrain(float val, float min, float max) {
		return Math.min(max, Math.max(min, val));
	}
}