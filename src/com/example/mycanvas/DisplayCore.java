package com.example.mycanvas;

public class DisplayCore
{
	private int direction = 1;
	private int blinkCounter;
	private int swingShowWidth = 0;
	private int count = 0;

	// Parameter
	final private int swingFrequency = 6;
	final private int density = 5;
	final private int blinkOffset = 100;
	final private int pointSize = 20;
	
	// Theorem
	final private int blinkFrequency = 1000 / ((swingFrequency * density) * 2);

	public void initial()
	{
		direction = 1;
		blinkCounter = 0;
		swingShowWidth = 0;
		count = 0;
	}
	
	public int getBlinkFrequency()
	{
		return blinkFrequency;
	}
	
	public int getPointSize()
	{
		return pointSize;
	}
}
