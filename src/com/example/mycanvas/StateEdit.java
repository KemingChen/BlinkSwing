package com.example.mycanvas;

import java.util.Timer;
import java.util.TimerTask;

import com.example.mycanvas.MyCanvas.StateName;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

public class StateEdit extends State
{

	public StateEdit(MyCanvas newCanvas)
	{
		super(newCanvas);
	}

	@Override
	public void startBlink()
	{
		int blinkFrequency = myCanvas.getDisplayCore().getBlinkFrequency();
		Timer timer = new Timer();
		TimerTask task = new BlinkTask(myCanvas);
		timer.schedule(task, 0, blinkFrequency);
		myCanvas.setTimer(timer);
		myCanvas.setState(StateName.RUN);
		myCanvas.getDisplayCore().initial();
	}

	@Override
	public void stopBlink()
	{	
	}

	@Override
	public void clear()
	{
		myCanvas.clearPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int pointSize = myCanvas.getDisplayCore().getPointSize();
		int x = ((int) event.getX() / pointSize) * pointSize;
		int y = ((int) event.getY() / pointSize) * pointSize;
		myCanvas.addPointInNowPage(new Rect(x, y, x + pointSize, y + pointSize));
		myCanvas.invalidate();
		return true;
		
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		
	}

}
