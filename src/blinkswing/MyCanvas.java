package blinkswing;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	private MyTimer timer = new MyTimer(this);
	private Map<StateName, State> states = new HashMap<StateName, State>();
	private StateName nowState;
	private CanvasPager pager = new CanvasPager(this);
	private BlinkCore blinkCore = new BlinkCore(this);
	private UIParameter uiParameter = new UIParameter();

	// Variable
	char lastDirect = ' ';

	// Parameter
	final double vibrationSensitivity = 2.0;

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

		pager.init();
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas)
	{
		states.get(nowState).onDraw(canvas);
	}

	public void OnChangeAcceleration(float value, long timestamp)
	{
		char direct = String.valueOf(value).toCharArray()[0] == '-' ? '-' : '+'; // record left or right // now direction
		float magnitude = Math.abs(value); // now magnitude

		if (magnitude >= vibrationSensitivity)
		{
			if (lastDirect != direct)
			{
				blinkCore.setDirection(direct == '-' ? 1 : -1, timestamp);
				lastDirect = direct;
			}
		}
		blinkCore.setMagnitude(magnitude);
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

	public void setTimer(MyTimer newTimer)
	{
		this.timer = newTimer;
	}

	public MyTimer getTimer()
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

	public CanvasPager getCanvasPager()
	{
		return pager;
	}
}
