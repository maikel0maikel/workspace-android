package com.bffmedia.hour6app;

import com.bffmedia.hour6app.R;
import com.bffmedia.hour6app.SettingActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button showFormButton=(Button)findViewById(R.id.showFormButton);
		showFormButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,FormControlActivity.class);
				startActivityForResult(intent,0);
			}
		});
		Button usingAdapter=(Button)findViewById(R.id.usingAdapterButton);
		usingAdapter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,UsingAdaptersActivity.class);
				startActivity(intent);
			}
		});
		Button progressBarButton=(Button)findViewById(R.id.progressbarButton);
		progressBarButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,ProgressActivity.class);
				startActivity(intent);
			}
		});
		Button imageViewButton=(Button)findViewById(R.id.imageViewButton);
		imageViewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,ImageViewActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		final Button ExerciseButton=(Button)findViewById(R.id.exerciseButton);
		ExerciseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,Excercise.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		SharedPreferences a=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		switch(Integer.parseInt(a.getString("layout", "1"))){
		case 1:
			ExerciseButton.setBackgroundResource(R.drawable.button_custom);
			break;
		case 2:
			ExerciseButton.setBackgroundResource(R.color.color_1);
			break;
		case 3:
			ExerciseButton.setBackgroundResource(R.color.color_2);
			break;
		}
		a.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
				// TODO Auto-generated method stub
				int k=Integer.parseInt(arg0.getString("layout", "1"));
				switch(k){
				case 1:
					ExerciseButton.setBackgroundResource(R.drawable.button_custom);
					break;
				case 2:
					ExerciseButton.setBackgroundResource(R.color.color_1);
					break;
				case 3:
					ExerciseButton.setBackgroundResource(R.color.color_2);
					break;
				}
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
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_settings:
			Intent intent=new Intent(this,SettingActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
