package blinkswing;

import java.util.List;

import com.example.mycanvas.R;

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
		((Button) findViewById(R.id.button_left)).setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myCanvas.prePage();
			}
		});
		((Button) findViewById(R.id.button_right)).setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myCanvas.nextPage();
			}
		});
		((Button) findViewById(R.id.button_new)).setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myCanvas.newPage();
			}
		});
		((Button) findViewById(R.id.button_delete)).setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myCanvas.delPage();
			}
		});
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

	protected void SetSensor()
	{
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) // if device has LINEAR_ACCELERATION
		{
			// Sensor Register
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
		float value = event.values[0]; // Grab x Axis
		myCanvas.OnChangeAcceleration(value);
	}
}