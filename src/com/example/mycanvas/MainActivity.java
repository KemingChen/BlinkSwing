package com.example.mycanvas;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity
{
	MyCanvas myCanvas;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		myCanvas = (MyCanvas) findViewById(R.id.myCanvas1);
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
		((ToggleButton) findViewById(R.id.toggleButton1)).setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        myCanvas.normalShow = !myCanvas.normalShow; 
		    }
		});;
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
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		System.out.println("onResume");
	}
}