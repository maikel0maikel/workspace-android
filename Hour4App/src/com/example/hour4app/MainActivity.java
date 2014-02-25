package com.example.hour4app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		 * add android:configChanges="orientation|screenSize"
		 * to manifest activity
		 */
		
		//click button
		Button toCam = (Button) findViewById(R.id.button_toCam);
		toCam.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intt=new Intent(MainActivity.this, CameraActivity.class);
				startActivity(intt);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		//Toast.makeText(this, "step2", Toast.LENGTH_SHORT).show();
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show();
			Log.w("qd", "detected landscape mode");
			setContentView(R.layout.activity_main);
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
	    	Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show();
	    	Log.w("qd", "detected portrait mode");
	    	setContentView(R.layout.activity_main);
	    }
	}
	/*
	protected void onSaveInstanceState(Bundle icicle) {
		  super.onSaveInstanceState(icicle);
		  Toast.makeText(this, "onSaveIn...", 3000);
	}
	*/

}
