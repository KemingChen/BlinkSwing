package blinkswing;

import android.graphics.Canvas;

public abstract class State
{
	protected MyCanvas myCanvas;

	public State(MyCanvas newCanvas)
	{
		this.myCanvas = newCanvas;;
	}
	public abstract void startBlink();
	public abstract void stopBlink();
	public abstract void clear();
	public abstract void onDraw(Canvas canvas);
	public abstract boolean onTouchEvent(android.view.MotionEvent event);
}
