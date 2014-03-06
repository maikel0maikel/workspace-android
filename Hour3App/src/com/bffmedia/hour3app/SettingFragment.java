package com.bffmedia.hour3app;

import com.bffmedia.hour3app.R;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Preference a=findPreference("edit_text");
		a.setSummary(a.getSharedPreferences().getString(a.getKey(), ""));
		a=findPreference("images");
		a.setSummary(((ListPreference)a).getEntry());
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
		Preference a=findPreference(key);
		if(key.equals("custom_text"))
		{
			if(sharedPreferences.getBoolean(key, false)){
				a.setSummary("Use custom text");
				findPreference("edit_text").setDefaultValue(((EditTextPreference)findPreference("edit_text")).getText());}
			else{
				a.setSummary("Use default text");
				a=findPreference("edit_text");
				a.setSummary("Click here to change your text");
				findPreference("edit_text").setDefaultValue("");
			}
		}
		else if(key.equals("edit_text"))
		{
			if(!sharedPreferences.getString(key, "").equals(""))
				a.setSummary(sharedPreferences.getString(key, ""));
			else
				a.setSummary("Click here to change your text");
		}
		else if(key.equals("images"))
		{
			a.setSummary(((ListPreference)a).getEntry());
		}
		
	}

}
