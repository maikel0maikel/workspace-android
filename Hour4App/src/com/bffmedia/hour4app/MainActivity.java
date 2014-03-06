package com.bffmedia.hour4app;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int numAttemps=0;
		if(savedInstanceState!=null){
			numAttemps=savedInstanceState.getInt("numAttempts");
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView t=(TextView)findViewById(R.id.textView1);
		t.setText("kim "+numAttemps);
		Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,CamCheck.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		PackageManager pm=getPackageManager();
		if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			t.setText("no camera found!");
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt("numAttempts",5);
	}
	@Override
	public  void  onConfigurationChanged(Configuration newConfig) {
	// TODO  Auto-generated method stub
	super.onConfigurationChanged(newConfig);
	//Toast.makeText(this, "step2", Toast.LENGTH_SHORT).show();

	if  (newConfig.orientation  == Configuration.ORIENTATION_LANDSCAPE) {
	Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show();
	Log.i("MainActivity","Landscape");
	setContentView(R.layout.cam_check);
	Intent intent=new Intent(MainActivity.this,CamCheck.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(intent);
	}
	else if  (newConfig.orientation  == Configuration.ORIENTATION_PORTRAIT) {
		Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show();
		Log.i("MainActivity","Portrait");
		setContentView(R.layout.activity_main);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
