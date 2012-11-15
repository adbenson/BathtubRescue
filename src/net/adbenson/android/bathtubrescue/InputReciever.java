package net.adbenson.android.bathtubrescue;

import net.adbenson.android.drawing.Vector;

public interface InputReciever {
	
	public void grabbed(Vector location);
	
	public void dragged(Vector location);
	
	public void dropped(Vector location);

}
