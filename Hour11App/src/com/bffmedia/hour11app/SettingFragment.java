package com.bffmedia.hour11app;

import com.example.hour11app.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class SettingFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	public static final String SETTING="com.bffmedia.hour11app.settings";
	public static final String FIRST_USE="com.bffmedia.hour11app.firstUse";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	@Override
	public void onResume(){
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	@Override
	public void onPause(){
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		Log.i("kienkimkhung_settings",key);
		SharedPreferences kim=PreferenceManager.getDefaultSharedPreferences(getActivity());
		String a=kim.getString("pie_type", "unknown");
		Toast.makeText(getActivity(), a, Toast.LENGTH_SHORT).show();
	}

}
