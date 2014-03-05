package com.example.hour15app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class QdSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new QdSettingFragment())
        .commit();
	}


}
