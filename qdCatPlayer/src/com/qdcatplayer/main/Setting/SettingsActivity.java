package com.qdcatplayer.main.Setting;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.R.string;
import com.qdcatplayer.main.R.xml;
import com.qdcatplayer.main.Setting.FolderChooserPreference.OnFolderChooserFinishListener;


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
