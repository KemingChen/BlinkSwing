package blinkswing;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class BlinkCore
{
	//private int direction;
	private int blinkCounter;
	private int count;
	private MyCanvas myCanvas;

	// Parameter
	final private int swingFrequency = 6;
	final private int density = 5;
	final private int blinkOffset = 100;
	
	// Theorem
	final private int blinkFrequency = 1000 / ((swingFrequency * density) * 2);

	public BlinkCore(MyCanvas myCanvas)
	{
		this.myCanvas = myCanvas;
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
		ArrayList<Rect> points = myCanvas.getCurrentPoints();
		Paint paint = myCanvas.getUIParameter().getPaint();
		int swingShowWidth;
		if (count == 1)
		{
			swingShowWidth = canvas.getWidth();
			blinkCounter += blinkOffset * myCanvas.getDirection();

			// if (blinkCounter > swingShowWidth - blinkOffset - swingShowWidth % blinkOffset)
			// blinkCounter = swingShowWidth - blinkOffset - swingShowWidth % blinkOffset;
			// else if (blinkCounter < 0 + blinkOffset)
			// blinkCounter = 0 + blinkOffset;

			if (blinkCounter > swingShowWidth - blinkOffset)
				myCanvas.setDirection(-1);
			else if (blinkCounter < 0 + blinkOffset)
				myCanvas.setDirection(1);

			for (Rect rect : points)
			{
				if (InBlinkRange(rect, swingShowWidth))
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
		return x > width && x < width + blinkOffset;
	}
}
