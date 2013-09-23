package com.example.mycanvas;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements SensorEventListener
{
	final int reverseRange = 8;// must be even
	final double vibrationSensitivity = 2.0;
	int count = 0;
	char gY[] = new char[reverseRange];
	char now;
	MyCanvas myCanvas;
	private SensorManager sensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		myCanvas = (MyCanvas) findViewById(R.id.myCanvas1);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		final Button startButton = ((Button) findViewById(R.id.button_start));

		((Button) findViewById(R.id.button1)).setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				System.out.println("onClearBtnClicked");
				myCanvas.clear();
			}
		});

		startButton.setOnClickListener(new Button.OnClickListener()
		{
			int counter = 0;

			@Override
			public void onClick(View v)
			{
				System.out.println("onStartClick");
				if (counter % 2 == 0)
				{
					myCanvas.startBlink();
					startButton.setText("Stop");
				}
				else
				{
					myCanvas.stopBlink();
					startButton.setText("Start");
				}
				counter++;
			}
		});
		((ToggleButton) findViewById(R.id.toggleButton1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				myCanvas.normalShow = !myCanvas.normalShow;
			}
		});
		;

		now = ' ';
		for (int i = 0; i < reverseRange; i++)
		{
			gY[i] = ' ';
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		myCanvas.stopBlink();
		System.out.println("onStop");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		myCanvas.stopBlink();
		System.out.println("onPause");
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		System.out.println("onResume");
		SetSensor();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@SuppressWarnings("deprecation")
	protected void SetSensor()
	{
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
		// 如果有取到該手機的方位感測器，就註冊他。
		if (sensors.size() > 0)
		{
			// registerListener必須要implements SensorEventListener，
			// 而SensorEventListener必須實作onAccuracyChanged與onSensorChanged
			// 感應器註冊
			sensorManager.registerListener(this, (Sensor) sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		float value = event.values[0]; //Grab x Axis
		if (Math.abs(value) >= vibrationSensitivity)
		{
			gY[count] = String.valueOf(value).toCharArray()[0] == '-' ? '-' : '+'; // record left or right

			if (now != gY[count])
			{
				myCanvas.ReverseDirection(gY[count] == '+' ? 1 : -1);
				now = gY[count];
			}

			// System.out.println(gY[count] + " " + String.valueOf(values[1])); // debug using
		}
		else
		{
			gY[count] = ' ';
		}
		count = (count + 1) % reverseRange;
	}
}