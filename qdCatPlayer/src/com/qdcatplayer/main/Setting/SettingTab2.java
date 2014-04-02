package com.qdcatplayer.main.Setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.qdcatplayer.main.R;

public class SettingTab2 extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure default values are applied.  In a real app, you would
        // want this in a shared function that is used to retrieve the
        // SharedPreferences wherever they are needed.
        //PreferenceManager.setDefaultValues(getActivity(),
        //        R.xml.advanced_preferences, false);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.setting_detail2);
    }
}
