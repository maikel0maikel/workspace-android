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
 * 
 * @author admin
 * 
 */
public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	private String prevLang = "en_US";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.setting_title));
		prevLang = PreferenceManager.getDefaultSharedPreferences(this).getString(LANG_KEY, "en_US");
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
	public static final String LANG_CHANGED_KEY = "languageChanged";
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
						.putExtra(LANG_CHANGED_KEY, !langChanged.equals(prevLang))
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
