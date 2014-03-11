package com.qdcatplayer.main;

import java.util.ArrayList;

import com.example.qdcatplayer.R;
import com.qdcatplayer.objects.MyPath;
import com.qdcatplayer.objects.MySong;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		MySong tmp=new MySong("/music/facebook_ringtone_pop.m4a");
		tmp.getPath();
		tmp.getAlbum();
		int i=0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
