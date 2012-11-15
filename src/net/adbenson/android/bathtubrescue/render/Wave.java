package net.adbenson.android.bathtubrescue.render;

import java.util.Random;

import net.adbenson.android.drawing.Drawable;
import net.adbenson.android.drawing.DrawingQueue;
import net.adbenson.android.drawing.DrawingQueueable;
import net.adbenson.android.drawing.Vector;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Camera.Area;


public class Wave implements Comparable, DrawingQueueable, AbstractModelRender{
//	private static final Area PROTO = generatePrototype();
//	private static final Path WAVE = generateWaveShape();
//	private static final Path WAVE_CREST = generateWaveCrest();
	
	public static final double swayRate = 0.1;
	
	private Path wave;
	private Path waveCrest;
	private int color;
	private double size;
		
	private boolean inWater;
	
	public Wave(int color, Vector location, double scale) {
//		shape = (Area) PROTO.clone();
//		wave = new Path(WAVE);
//		waveCrest = new Path(WAVE_CREST);
//		
//		AffineTransform scaler = AffineTransform.getScaleInstance(scale, scale);
//		
//		shape.transform(scaler);
//		wave.transform(scaler);
//		waveCrest.transform(scaler);
		
		this.color = color;
		this.size = scale;
		
		inWater = true;
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
	
	public void enqueueForDraw(DrawingQueue queue) {
		queue.add(new Drawable(5) {
			public void draw(Canvas g) {
//				
//				g.setTransform(AffineTransform.getTranslateInstance(tempLocation.x, tempLocation.y));
//						
//				g.setStroke(new BasicStroke(1));
//				g.setColor(color);
//				g.fill(shape);
//				g.setColor(Color.BLACK);
//				g.draw(shape);
//				
//				if (inWater) {
//					g.setColor(Color.BLUE);
////					g.setTransform(AffineTransform.getTranslateInstance(location.x, location.y));
//					g.fill(wave);
//					g.setColor(Color.BLACK);
//					g.draw(waveCrest);
//				}
			}
		});
	}

	public int compareTo(Object arg0) {
		return (int) Math.signum(((Wave)arg0).size - this.size);
	}
	
	public Rect getBounds() {
//		Rectangle bounds = shape.getBounds();
//		bounds.translate(location.intX(), location.intY());
//		return bounds;
		return null;
	}

	public void draw(Canvas g) {
		// TODO Auto-generated method stub
		
	}

	public boolean intersects(AbstractModelRender that) {
		// TODO Auto-generated method stub
		return false;
	}
}
