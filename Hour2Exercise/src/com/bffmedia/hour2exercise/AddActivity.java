package com.bffmedia.hour2exercise;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
public class AddActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		TextView t=(TextView)findViewById(R.id.textView1);
		Intent intent=getIntent();
		Bundle bundle=intent.getBundleExtra("bundle");
		int num1=bundle.getInt("num1",0);
		int num2=bundle.getInt("num2",0);
		t.setText("The result is: "+(num1+num2));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input, menu);
		return true;
	}

}
