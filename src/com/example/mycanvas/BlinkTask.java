package com.example.mycanvas;

import java.util.TimerTask;

public class BlinkTask extends TimerTask
{
	private MyCanvas myCanvas;

	public BlinkTask(MyCanvas myCanvas)
	{
		this.myCanvas = myCanvas;
	}
	
	@Override
	public void run()
	{
		myCanvas.sendBlinkMessageThroughtHandler();
	}
}
