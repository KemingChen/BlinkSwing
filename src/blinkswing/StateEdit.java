package blinkswing;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import blinkswing.MyCanvas.StateName;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class StateEdit extends State
{
	private Paint paint;
	private CanvasPager pager;
	private int pointSize;

	public StateEdit(MyCanvas newCanvas,UIParameter parameter)
	{
		super(newCanvas);
		this.paint = parameter.getPaint();
		this.pointSize = parameter.getPointSize();
		this.pager = myCanvas.getCanvasPager();
	}

	@Override
	public void startBlink()
	{
		int blinkFrequency = myCanvas.getBlinkCore().getBlinkFrequency();
		MyTimer timer = new MyTimer(myCanvas);
		timer.setPeriod(blinkFrequency, 0);
		timer.start();
		myCanvas.setTimer(timer);
		myCanvas.setState(StateName.RUN);
		myCanvas.getBlinkCore().initial();
	}

	@Override
	public void stopBlink()
	{	
	}

	@Override
	public void clear()
	{
		pager.clearPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int x = ((int) event.getX() / pointSize) * pointSize;
		int y = ((int) event.getY() / pointSize) * pointSize;
		pager.addPointInCurrentPage(new Rect(x, y, x + pointSize, y + pointSize));
		myCanvas.invalidate();
		return true;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		ArrayList<Rect> points = pager.getCurrentPoints();
		for (int i = 0; i < points.size(); i++)
		{
			canvas.drawRect(points.get(i), paint);
		}
	}
}
