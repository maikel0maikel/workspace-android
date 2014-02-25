package com.example.hour6app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tv=null;
	AutoCompleteTextView atv=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String tmp[] = {"Huế", "Hà Nội", "Dà Nẵng", "Hồ Chí Minh", "Dai Tay"};
		
		tv=(TextView)findViewById(R.id.textView1);
		atv=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>  
        (this,android.R.layout.select_dialog_item,tmp);
		
		atv.setAdapter(adapter);
		
		
		atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	        @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
	        	tv.setText(atv.getText().toString());
			}
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
