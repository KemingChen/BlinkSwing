package com.example.mycanvas;

import com.example.mycanvas.MyCanvas.StateName;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class StateRun extends State
{

	public StateRun(MyCanvas newCanvas)
	{
		super(newCanvas);
	}

	@Override
	public void startBlink()
	{
	}

	@Override
	public void stopBlink()
	{
		myCanvas.setState(StateName.EDIT);
		myCanvas.getTimer().cancel();
	}

	@Override
	public void clear()
	{
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return false;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		
	}

}
