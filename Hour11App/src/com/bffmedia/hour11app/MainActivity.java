package com.bffmedia.hour11app;

import java.util.Calendar;

import com.example.hour11app.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String SETTING="com.bffmedia.hour11app.settings";
	public static final String FIRST_USE="com.bffmedia.hour11app.firstUse";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences preferences=getSharedPreferences(SETTING, MODE_PRIVATE);
		Boolean firstUse=preferences.getBoolean(FIRST_USE, true);
		if(firstUse){
			Toast.makeText(getApplicationContext(), "hello first use", Toast.LENGTH_SHORT).show();
			setVar();
		}
	
	}


	public void setVar()
	{
		SharedPreferences preferences=getSharedPreferences(SETTING, MODE_PRIVATE);
		Editor edit=preferences.edit();
		edit.putBoolean(FIRST_USE, true);
		edit.commit();
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
			Intent intent=new Intent(this,SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
