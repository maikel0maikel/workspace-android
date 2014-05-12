package com.qdcatplayer.main.Setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.qdcatplayer.main.R;

public class SettingTab2 extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.setting_detail2);
	}
}
