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

	// Test
	public boolean normalShow = true;// for test

	// Object
	private Mode mode;
	private Timer timer = new Timer();
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ArrayList<Rect> points = new ArrayList<Rect>();
	private ArrayList<ArrayList<Rect>> pointPages;

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
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
		paint.setColor(Color.WHITE);
		mode = Mode.EDIT;
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
		if (mode == Mode.EDIT)
		{
			for (int i = 0; i < points.size(); i++)
			{
				canvas.drawRect(points.get(i), paint);
			}
		}
		else if (mode == Mode.RUN && count == 1)
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
		if (mode != Mode.EDIT)
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

	public void clear()
	{
		if (mode != Mode.EDIT)
			return;
		pointPages.get(nowPage).clear();
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
}
