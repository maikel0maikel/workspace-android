package com.example.hour4app;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		int numCameras = Camera.getNumberOfCameras();
		Boolean hasCam=false;
		Boolean frontCam=false;
		Boolean backCam=false;
		Boolean flashSupport=this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		//hascam=this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
		
		
		if (numCameras >0) {
		  hasCam = true;
		}
		
		for(int i=0;i<numCameras;i++){
		    Camera.CameraInfo info = new CameraInfo();
		    Camera.getCameraInfo(i, info);
		    if(CameraInfo.CAMERA_FACING_FRONT == info.facing){
		        frontCam=true;
		    }
		    if(CameraInfo.CAMERA_FACING_BACK == info.facing){
		        backCam=true;
		    }
		    
		}
		//Log.w("qdCamera", String.valueOf(numCameras));
		TextView maint=(TextView)findViewById(R.id.textView_cam);
		maint.setText(
				"Number of Cams: " + numCameras + "\r\n" +
				"Front Cam: " + (frontCam?"Yes":"No") + "\r\n"+
				"Back Cam: " + (backCam?"Yes":"No") + "\r\n" +
				"Flash support: " + (flashSupport?"Yes":"No")
				);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

}
