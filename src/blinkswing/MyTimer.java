package blinkswing;

public class MyTimer extends Thread
{
	private MyCanvas myCanvas;
	private boolean isCanceled;
	private int period_ms;
	private int period_us;
	
	public MyTimer(MyCanvas myCanvas)
	{
		period_ms = 1000;
		period_us = 0;
		this.myCanvas = myCanvas;
	}

	public void run()
	{
		long testStartTime = System.currentTimeMillis();
		int testCount = 0;
		while (!isCanceled)
		{
			try
			{
				myCanvas.sendBlinkMessageThroughtHandler();
				testCount++;
				Thread.sleep(period_ms, period_us);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println((System.currentTimeMillis()-testStartTime));
		System.out.println(testCount);
	}

	public void cancel()
	{
		isCanceled = true;
	}

	public void setPeriod(int period_ms, int period_us)
	{
		this.period_ms = period_ms;
		this.period_us = period_us;
	}
}
