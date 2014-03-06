package com.bffmedia.hour4app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class CamCheck extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cam_check);
		TextView t=(TextView)findViewById(R.id.textView1);
		PackageManager pm=getPackageManager();
		int cam_num=0;
		String front_cam="Không";
		String autofocus="Không";
		String flash="Không";
		if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			cam_num=Camera.getNumberOfCameras();
			if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))
				front_cam="Có";
			if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
				flash="Có";
			if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS))
				autofocus="Có";
		}
		t.setText("Số camera: "+cam_num+" \nCamera trước: "+front_cam+" \nHỗ trợ Flash: "+flash+" \nAutoFocus: "+autofocus);
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
	finish();
	}
	else if  (newConfig.orientation  == Configuration.ORIENTATION_PORTRAIT) {
		Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show();
		Log.i("MainActivity","Portrait");
		setContentView(R.layout.activity_main);
		Intent intent=new Intent(CamCheck.this,MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
