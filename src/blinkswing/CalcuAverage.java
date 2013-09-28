package blinkswing;

public class CalcuAverage
{
	private int range;
	private long[] array;
	private int countor;

	public CalcuAverage(int range, long initialValue)
	{
		this.range = range;
		countor = 0;
		array = new long[range];
		for (int i = 0; i < range; i++)
		{
			array[i] = initialValue;
		}
	}

	public long getValue()
	{
		long total = 0;
		int counter = 0;
		long std = getStandardDeviation();
		long avg = getAverage();
		long min = avg - std;
		long max = avg + std;

		for (long value : array)
		{
			if (value >= min && value <= max)
			{
				total += value;
				counter++;
			}
		}

		// System.out.println("str&avg : " + std + ", " + avg);
		// System.out.println("min&max : " + min + ", " + max);
		return total / (counter > 0 ? counter : 1);
	}

	public void saveValue(long value)
	{
		array[countor] = value;
		// System.out.println("save: " + value);
		countor = (countor + 1) % range;
	}

	private long getAverage()
	{
		long total = 0;
		for (long value : array)
		{
			total += value;
		}
		return total / array.length;
	}

	private long getStandardDeviation()
	{
		long total = 0;
		long avg = getAverage();

		for (long value : array)
		{
			total += value * value;
		}
		return (long) Math.sqrt(total / array.length - avg * avg);
	}
}
