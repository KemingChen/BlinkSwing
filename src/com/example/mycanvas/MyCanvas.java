package com.example.mycanvas;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class MyCanvas extends View
{
	public enum Mode
	{
		EDIT, RUN,
	}

	public boolean normalShow = true;//for test
	private int direction = 1;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ArrayList<Rect> points = new ArrayList<Rect>();
	private Mode mode;
	private Timer timer = new Timer();
	private int blinkCounter;
	final private int blinkOffset = 20;
	final private int pointSize = 10;
	final private int blinkFrequency = 1000;
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			invalidate();
		}
	};

	public MyCanvas(Context context)
	{
		super(context);
		init();
	}

	public MyCanvas(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public MyCanvas(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setFocusable(true);
		init();
	}

	private void init()
	{
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
		paint.setColor(Color.WHITE);
		mode = Mode.EDIT;
		blinkCounter = 0;
		this.setBackgroundColor(Color.BLACK);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (mode == Mode.EDIT)
		{
			for (int i = 0; i < points.size(); i++)
			{
				canvas.drawRect(points.get(i), paint);
			}
		}
		else if (mode == Mode.RUN)
		{
			blinkCounter += blinkOffset*direction;
			if(blinkCounter > canvas.getWidth() || blinkCounter < 0)
				direction *= -1;
			int displayPointsCounter = 0;
			for (Rect rect : points)
			{
				if (InBlinkRange(rect, canvas.getWidth()))
				{
					if (normalShow)
					{
						canvas.drawRect(rect, paint);
					}
					else
					{
						int width = canvas.getWidth();
						Rect tempRect = new Rect(rect);
						tempRect.left = (width - pointSize) / 2 + displayPointsCounter;
						tempRect.right = (width + pointSize) / 2 + displayPointsCounter;
						displayPointsCounter++;
						canvas.drawRect(tempRect, paint);
					}
				}
			}
		}
	}

	private boolean InBlinkRange(Rect rect, int canvasWidth)
	{
		int width = blinkCounter % canvasWidth;
		int x = rect.centerX();
		return x < width + blinkOffset && x > width - blinkOffset;
	}

	public void ReverseDirection(int direction)
	{
		this.direction = direction;
		if (direction == -1)
		{
			System.out.println("right");
		}
		else
		{
			System.out.println("left");
		}
	}
	
	@Override
	public boolean onTouchEvent(android.view.MotionEvent event)
	{
		if (mode != Mode.EDIT)
			return false;
		int x = (int) event.getX();
		int y = (int) event.getY();
		points.add(new Rect(x, y, x + pointSize, y + pointSize));
		this.invalidate();
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}

	public void clear()
	{
		if (mode != Mode.EDIT)
			return;
		points.clear();
		this.invalidate();
	}

	public void startBlink()
	{
	if (mode == Mode.RUN)
			return;
		mode = Mode.RUN;
		timer = new Timer();
		TimerTask task = new BlinkTask(this);
		timer.schedule(task, 0, blinkFrequency);
		System.out.println("start blink done");
	}

	public void sendBlinkMessageThroughtHandler()
	{
		handler.sendEmptyMessage(1);
	}

	public void stopBlink()
	{
		if (mode != Mode.RUN)
			return;
		mode = Mode.EDIT;
		timer.cancel();
		blinkCounter = 0;
	}

	public Mode getMode()
	{
		return this.mode;
	}
}
