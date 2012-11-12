package net.adbenson.android.drawing;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import android.graphics.Canvas;

public class DrawingQueue {
	
	private static Comparator<Drawable> comparator;
	
	private LinkedList<Drawable> list;
	
	private Canvas canvas;
	
	public DrawingQueue(Canvas canvas) {

		list = new LinkedList<Drawable>();
		
		this.canvas = canvas;
	}
	
	public void add(Drawable d) {
		list.add(d);
	}
	
	public void add(Collection<? extends DrawingQueueable> dq) {
		for(DrawingQueueable obj : dq) {
			add(obj);
		}
	}
	
	public void draw() {
		Collections.sort(list, getComparator());
		
		while(! list.isEmpty()) {
			Drawable d = list.poll();
			d.triggerDraw(canvas);
		}
	}

	public void add(DrawingQueueable de) {
		de.enqueueForDraw(this);
	}
	
	public void setComparator(Comparator<Drawable> comparator) {
		DrawingQueue.comparator = comparator;
	}
	
	public Comparator<Drawable> getComparator() {
		if (comparator == null) {
			comparator = new Drawable.Compare();
		}
		
		return comparator;
	}
}
