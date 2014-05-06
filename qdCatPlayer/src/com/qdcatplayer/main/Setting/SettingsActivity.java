package com.qdcatplayer.main.Setting;

import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.qdcatplayer.main.R;

/**
 * Caller activity must use startActivity for result
 * @author admin
 *
 */
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				 
	}
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.setting_header, target);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.w("qd", this.getClass().getName());
		//setresult here
		Boolean changed = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(FolderChooserPreference.FOLDER_CHANGED_KEY,false);
		setResult(RESULT_OK,
				getIntent().putExtra(FolderChooserPreference.FOLDER_CHANGED_KEY, changed)
				);
		return;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
