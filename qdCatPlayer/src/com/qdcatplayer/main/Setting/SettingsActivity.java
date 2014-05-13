package com.qdcatplayer.main.Setting;

import java.util.List;
import java.util.Locale;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.qdcatplayer.main.R;

/**
 * Caller activity must use startActivity for result
 * 
 * @author admin
 * 
 */
public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.setting_title));
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
	public static final String LANG_KEY = "languageList";
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.w("qd", this.getClass().getName());
		// setresult here
		Boolean dbChanged = PreferenceManager.getDefaultSharedPreferences(this)
				.getBoolean(FolderChooserPreference.FOLDER_CHANGED_KEY, false);
		String langChanged = PreferenceManager.getDefaultSharedPreferences(this)
				.getString(LANG_KEY, "en_US");
		
		//cho activity biet la DB da thay doi, can refresh
		setResult(
				RESULT_OK,
				getIntent().putExtra(
						FolderChooserPreference.FOLDER_CHANGED_KEY, dbChanged)
						.putExtra(LANG_KEY, langChanged)
				);
		return;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
