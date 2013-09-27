package blinkswing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	private Map<StateName, State> states = new HashMap<StateName, State>();
	private StateName nowState;
	private CanvasPager pager = new CanvasPager(this);
	private BlinkCore blinkCore = new BlinkCore(this);
	private UIParameter uiParameter = new UIParameter();
	
	// Variable
	private int direction = 1;
	char lastDirect = ' ';

	// Parameter
	final double vibrationSensitivity = 10.0;

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
		states.put(StateName.EDIT, new StateEdit(this, uiParameter));
		states.put(StateName.RUN, new StateRun(this));

		nowState = StateName.EDIT;
		this.setBackgroundColor(Color.BLACK);

		direction =1;
		pager.init();
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas)
	{
		states.get(nowState).onDraw(canvas);
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
			// System.out.println(direct + " " + String.valueOf(value)); // debug using
		}
	}

	@Override
	public boolean onTouchEvent(android.view.MotionEvent event)
	{
		return states.get(nowState).onTouchEvent(event);
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

	public BlinkCore getBlinkCore()
	{
		return blinkCore;
	}

	public UIParameter getUIParameter()
	{
		return uiParameter;
	}
	
	public int getDirection()
	{
		return direction;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}

	public CanvasPager getCanvasPager()
	{
		return pager;
	}

}
