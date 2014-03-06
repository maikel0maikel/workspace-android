package com.bffmedia.hour5app;

import com.bffmedia.hour5app.R;
import com.bffmedia.hour5app.SettingActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences a=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		int k=Integer.parseInt(a.getString("layout", "1"));
		switch(k){
		case 2:
			setContentView(R.layout.excercise1_frame_layout);
			break;
		case 3:
			setContentView(R.layout.excercise1_relative_layout);
			break;
		case 4:
			setContentView(R.layout.exercise2_framelayout);
			break;
		case 5:
			setContentView(R.layout.excercise3_relativelayout);
			break;
		default:
			setContentView(R.layout.activity_main);
		}
		a.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
				// TODO Auto-generated method stub
				int k=Integer.parseInt(arg0.getString("layout", "1"));
				switch(k){
				case 2:
					setContentView(R.layout.excercise1_frame_layout);
					break;
				case 3:
					setContentView(R.layout.excercise1_relative_layout);
					break;
				case 4:
					setContentView(R.layout.exercise2_framelayout);
					break;
				case 5:
					setContentView(R.layout.excercise3_relativelayout);
					break;
				default:
					setContentView(R.layout.activity_main);
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
