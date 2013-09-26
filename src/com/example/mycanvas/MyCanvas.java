package com.example.mycanvas;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
	public enum StateName
	{
		EDIT, RUN,
	}

	// Test
	public boolean normalShow = true;// for test

	// Object
	private Timer timer = new Timer();
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ArrayList<ArrayList<Rect>> pointPages;
	private Map<StateName, State> states = new HashMap<StateName, State>();
	private StateName nowState;
	private DisplayCore displayCore = new DisplayCore();

	// Variable
	private int nowPage = 0;
	private int direction = 1;
	private int blinkCounter;
	private int swingShowWidth = 0;
	private int count = 0;
	char lastDirect = ' ';

	// Parameter
	final private int swingFrequency = 6;
	final private int density = 5;
	final private int blinkOffset = 100;
	final private int pointSize = 20;
	final double vibrationSensitivity = 10.0;

	// Theorem
	final private int blinkFrequency = 1000 / ((swingFrequency * density) * 2);

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
		states.put(StateName.EDIT, new StateEdit(this));
		states.put(StateName.RUN, new StateRun(this));
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
		paint.setColor(Color.WHITE);
		nowState = StateName.EDIT;
		blinkCounter = 0;
		this.setBackgroundColor(Color.BLACK);

		pointPages = new ArrayList<ArrayList<Rect>>();
		pointPages.add(new ArrayList<Rect>());
		nowPage = 0;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		ArrayList<Rect> points = pointPages.get(nowPage);
		if (nowState == StateName.EDIT)
		{
			for (int i = 0; i < points.size(); i++)
			{
				canvas.drawRect(points.get(i), paint);
			}
		}
		else if (nowState == StateName.RUN && count == 1)
		{
			int displayPointsCounter = 0;
			swingShowWidth = canvas.getWidth();

			blinkCounter += blinkOffset * direction;

			// if (blinkCounter > swingShowWidth - blinkOffset - swingShowWidth % blinkOffset)
			// blinkCounter = swingShowWidth - blinkOffset - swingShowWidth % blinkOffset;
			// else if (blinkCounter < 0 + blinkOffset)
			// blinkCounter = 0 + blinkOffset;

			if (blinkCounter > swingShowWidth - blinkOffset)
				direction = -1;
			else if (blinkCounter < 0 + blinkOffset)
				direction = 1;

			for (Rect rect : points)
			{
				if (InBlinkRange(rect, swingShowWidth))
				{
					if (normalShow)
					{
						canvas.drawRect(rect, paint);
					}
					else
					{
						int width = canvas.getWidth();
						Rect tempRect = new Rect(rect);
						tempRect.left += width / 2 - blinkCounter % canvas.getWidth() - blinkOffset / 2;
						tempRect.right += width / 2 - blinkCounter % canvas.getWidth() - blinkOffset / 2;
						displayPointsCounter++;
						canvas.drawRect(tempRect, paint);
					}
				}
			}
		}
		count = (count + 1) % density;
	}

	private boolean InBlinkRange(Rect rect, int canvasWidth)
	{
		int width = blinkCounter;
		int x = rect.centerX();
		return x > width && x < width + blinkOffset;
	}

	public void OnChangeAcceleration(float value)
	{
		char direct = String.valueOf(value).toCharArray()[0] == '-' ? '-' : '+'; // record left or right // now direction
		float magnitude = Math.abs(value); // now magnitude

		if (magnitude >= vibrationSensitivity)
		{
			if (lastDirect != direct)
			{
				direction = direct == '-' ? 1 : -1;
				if (direction == 1)
				{
					// blinkCounter = 0 + blinkOffset;
					System.out.println("right");
				}
				else
				{
					// blinkCounter = swingShowWidth - blinkOffset - swingShowWidth % blinkOffset;
					System.out.println("left");
				}
				lastDirect = direct;
			}
			//System.out.println(direct + " " + String.valueOf(value)); // debug using
		}
	}

	@Override
	public boolean onTouchEvent(android.view.MotionEvent event)
	{
		if (nowState != StateName.EDIT)
			return false;
		int x = ((int) event.getX() / pointSize) * pointSize;
		int y = ((int) event.getY() / pointSize) * pointSize;
		pointPages.get(nowPage).add(new Rect(x, y, x + pointSize, y + pointSize));
		this.invalidate();
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}

	public void sendBlinkMessageThroughtHandler()
	{
		handler.sendEmptyMessage(1);
	}

	public void clear()
	{
		states.get(nowState).clear();
	}

	public void startBlink()
	{
		states.get(nowState).startBlink();
	}

	public void stopBlink()
	{
		states.get(nowState).stopBlink();
	}

	public void nextPage()
	{
		nowPage = (nowPage + 1) % pointPages.size();
		this.invalidate();
	}

	public void prePage()
	{
		nowPage = (nowPage + pointPages.size() - 1) % pointPages.size();
		this.invalidate();
	}

	public void newPage()
	{
		pointPages.add(nowPage, new ArrayList<Rect>());
		this.invalidate();
	}

	public void delPage()
	{
		if (pointPages.size() > 1)
		{
			pointPages.remove(nowPage);
			this.invalidate();
		}
	}

	public void clearPage()
	{
		pointPages.get(nowPage).clear();
		this.invalidate();
	}

	public void setState(StateName newState)
	{
		nowState = newState;
	}

	public void setTimer(Timer newTimer)
	{
		this.timer = newTimer;
	}

	public Timer getTimer()
	{
		return timer;
	}

	public DisplayCore getDisplayCore()
	{
		return displayCore;
	}

	public void addPointInNowPage(Rect rect)
	{
		pointPages.get(nowPage).add(rect);
	}
}
