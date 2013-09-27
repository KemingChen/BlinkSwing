package blinkswing;

import android.graphics.Color;
import android.graphics.Paint;

public class UIParameter
{
	final private int pointSize = 20;
	final private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public UIParameter()
	{
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
		paint.setColor(Color.WHITE);
	}

	public Paint getPaint()
	{
		return paint;
	}

	public int getPointSize()
	{
		return pointSize;
	}
}
