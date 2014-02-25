package com.example.qd;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class AddActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity2);
		setTitle("AddActivity");
		//get bundle
		Bundle bdl= this.getIntent().getExtras();
		//get value
		int so1 = bdl.getInt("so1");
		int so2 = bdl.getInt("so2");
		int kqi=so1+so2;
		EditText kq=(EditText)findViewById(R.id.editText_ketqua);
		kq.setText(String.valueOf(kqi));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity2, menu);
		return true;
	}

}
