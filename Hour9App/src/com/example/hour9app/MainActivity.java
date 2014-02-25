package com.example.hour9app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements QdDateEnteredListener {
	TextView tv;
	Button bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv=(TextView)findViewById(R.id.textView);
		bt=(Button)findViewById(R.id.button1);
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				QdDatePickerDialogFragment dg=new QdDatePickerDialogFragment();
				dg.show(getFragmentManager(), "my datepicker dialog");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void OnDateEntered(int year, int month, int day) {
		// TODO Auto-generated method stub
		month++;//thang thu 0
		tv.setText(year+"-"+month+"-"+day);
	}

}
