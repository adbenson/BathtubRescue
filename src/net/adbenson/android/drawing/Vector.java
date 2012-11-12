package net.adbenson.android.drawing;

import android.graphics.PointF;


public class Vector extends PointF {
		
	static final double FULL_CIRCLE = 2 * Math.PI;
	
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

	public double getAngle() {
		Vector normal = this.normalize();
		double angle = (FULL_CIRCLE - Math.atan2(normal.x, normal.y));				
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
	
	public double magnitude() {
		return this.length();
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