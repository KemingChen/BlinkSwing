package blinkswing;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class BlinkCore
{
	private int direction;
	private float magnitude;
	private int blinkCounter;
	private int count;
	private MyCanvas myCanvas;
	private CanvasPager pager;

	// Parameter
	final private int swingFrequency = 6;
	final private int density = 5;
	final private int blinkOffset = 100;

	// Theorem
	final private int blinkFrequency = 1000 / ((swingFrequency * density) * 2);

	public BlinkCore(MyCanvas myCanvas)
	{
		this.myCanvas = myCanvas;
		this.pager = myCanvas.getCanvasPager();
		initial();
	}

	public void initial()
	{
		blinkCounter = 0;
		count = 0;
	}

	public int getBlinkFrequency()
	{
		return blinkFrequency;
	}

	public void onBlinkDraw(Canvas canvas)
	{
		ArrayList<Rect> points = pager.getCurrentPoints();
		Paint paint = myCanvas.getUIParameter().getPaint();
		int canvasWidth;
		if (count == 1)
		{

			canvasWidth = canvas.getWidth();
			blinkCounter = blinkCounter + (blinkOffset * direction) % canvasWidth;

			// if (blinkCounter > swingShowWidth - blinkOffset - swingShowWidth % blinkOffset)
			// blinkCounter = swingShowWidth - blinkOffset - swingShowWidth % blinkOffset;
			// else if (blinkCounter < 0 + blinkOffset)
			// blinkCounter = 0 + blinkOffset;

			if (blinkCounter > canvasWidth)
			{
				if (pager.isLastPage())
				{
					direction = 0;
					// myCanvas.setDirection(-1);
				}
				else
				{
					pager.nextPage();
					blinkCounter = 0;
				}
			}
			else if (blinkCounter < 0)
			{
				if (pager.isFirstPage())
				{
					direction = 0;
					// myCanvas.setDirection(1);
				}
				else
				{
					pager.prePage();
					blinkCounter = canvasWidth - canvasWidth % blinkOffset + blinkOffset;
				}
			}

			for (Rect rect : points)
			{
				if (InBlinkRange(rect, canvasWidth))
				{
					if (myCanvas.normalShow)
					{
						canvas.drawRect(rect, paint);
					}
					else
					{
						int width = canvas.getWidth();
						Rect tempRect = new Rect(rect);
						tempRect.left += width / 2 - blinkCounter % canvas.getWidth() - blinkOffset / 2;
						tempRect.right += width / 2 - blinkCounter % canvas.getWidth() - blinkOffset / 2;
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
		if (blinkCounter >= 0)
			return x > width && x < width + blinkOffset;
		else
			return false;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;

		// Test
		if (direction == 1)
		{
			System.out.println("right");
		}
		else
		{
			System.out.println("left");
		}
	}

	public void setMagnitude(float magnitude)
	{
		this.magnitude = magnitude;
	}
}
