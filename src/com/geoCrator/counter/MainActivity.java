package com.geoCrator.counter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	SensorManager sm;
	Sensor s;
	int counter = 0;
	int trials[][] = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
	int tcount = 0;
	int temp1;
	int temp2;
	boolean flag = false;
	TextView count;
	TextView status;
	Button countButton;
	ProgressBar counting;
	RadioButton radArray[] = new RadioButton[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		count = (TextView) findViewById(R.id.textView2);
		status = (TextView) findViewById(R.id.textView5);
		countButton = (Button) findViewById(R.id.button8);
		countButton.setText("COUNT");
		radArray[0] = (RadioButton) findViewById(R.id.radioButton1);
		radArray[1] = (RadioButton) findViewById(R.id.radioButton2);
		radArray[2] = (RadioButton) findViewById(R.id.radioButton3);
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		counting = (ProgressBar) findViewById(R.id.progressBar1);
		counting.setVisibility(View.INVISIBLE);
		if (sm.getSensorList(Sensor.TYPE_PROXIMITY).size() != 0) {
			s = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
			for (int i = 0; i < 3; i++){
				radArray[i].setVisibility(View.INVISIBLE);
				radArray[i].isChecked();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void countClick(View view) {
		if (flag) {
			onPause();
			countButton.setText("RECOUNT");
			tcount++;
			sort();
			for (int i = 0; i < 3; i++) {
				radArray[i].setText("Trial " + trials[i][1] + " :   "
						+ trials[i][0]);
				if (trials[i][1] != 0)
					radArray[i].setVisibility(View.VISIBLE);
			}
			
		} else {
			counter = 0;
			count.setText(counter + "");
			countButton.setText("STOP");
			resume();
		}

	}

	public void reset(View view) {
		onPause();
		counter = 0;
		tcount = 0;
		countButton.setText("COUNT");
		count.setText(counter + "");
		for (int i = 0; i < 3; i++) {
			radArray[i].setVisibility(View.INVISIBLE);
			trials[i][0] = 0;
			trials[i][1] = 0;
		}

	}

	/*public void stop(View view) {
		if (flag) {
			onPause();
			countButton.setText("RECOUNT");
			tcount++;
			sort();
			for (int i = 0; i < 3; i++) {
				radArray[i].setText("Trial " + trials[i][1] + " :   "
						+ trials[i][0]);
				if (trials[i][1] != 0)
					radArray[i].setVisibility(View.VISIBLE);
			}
			status.setText("Final Count =" + trials[0][0]);

		}
	}*/
	
	public void done(View view) {
		status.setText("Final Count =" + trials[0][0]);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		sm.unregisterListener(this, s);
		counting.setVisibility(View.INVISIBLE);
		status.setText("IDLE...");
		flag = false;
		super.onPause();
	}

	protected void resume() {
		// TODO Auto-generated method stub
		counting.setVisibility(View.VISIBLE);
		status.setText("COUNTING....");
		flag = true;
		sm.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent flick) {
		// TODO Auto-generated method stub
		if (flick.values[0] == 0) {
			counter++;
			count.setText(counter + "");
		}

	}

	public void sort() {
		int i=3;
		while(i>0){
			i--;
			if(counter > trials[i][0]) {
				temp1 = trials[i][0];
				temp2 = trials[i][1];
				trials[i][0] = counter;
				trials[i][1] = tcount;
				if(i<2) {
					trials[i + 1][0] = temp1;
					trials[i + 1][1] = temp2;
				} 
			} else break;
		}
	}
}
