package net.adbenson.android.drawing;

import android.graphics.PointF;


public class Vector extends PointF {
		
	static final float FULL_CIRCLE = (float) (2f * Math.PI);
	
	public Vector() {
		this(0, 0);
	}
	
	public Vector(double x, double y) {
		this((float)x, (float)y);
	}
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector invert() {
		return new Vector(-x, -y);
	}

	public float getAngle() {
		Vector normal = this.normalize();
		float angle = (float) (FULL_CIRCLE - Math.atan2(normal.x, normal.y));				
		return angle;
	}

	public Vector normalize() {
		double length = this.magnitude();
		if (length != 0) {
			return new Vector(x / length, y / length);
		}
		else {
			return new Vector(0, 0);
		}
	}
	
	public float magnitude() {
		return this.length();
	}
	
	public float length(Vector that) {
		return (float) Math.sqrt(
				Math.pow(this.x - that.x, 2)+
				Math.pow(this.y - that.y, 2)
				);
	}
	
	public Vector scale(double delta) {
		return new Vector(x*delta, y*delta);
	}
	
	public Vector add(Vector position) {
		return add(position.x, position.y);
	}
	
	public Vector subtract(Vector vector) {
		return new Vector(this.x - vector.x, this.y - vector.y);
	}
	
	public int intX() {
		return (int) Math.round(x);
	}
	
	public int intY() {
		return (int) Math.round(y);
	}

	public Vector add(double x, double y) {
		return new Vector(this.x + x, this.y + y);
	}

	public double normalDistance(Vector that) {
		Vector thisNorm = this.normalize();
		Vector thatNorm = that.normalize();
		Vector difference = thisNorm.subtract(thatNorm);
		return difference.magnitude();
	}

}