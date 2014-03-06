package com.bffmedia.hour6app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class UsingAdaptersActivity extends Activity {
 String []values={"one","two","three","four","five","six","one hundred"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.using_adapters_layout);
		final Spinner mSpinner = (Spinner)findViewById(R.id.spinner1);
		ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,values);
		mSpinner.setAdapter(spinnerAdapter);
		final AutoCompleteTextView mAutoText=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		ArrayAdapter<String> autoTextAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,values);
		mAutoText.setAdapter(autoTextAdapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String selected=(String)mSpinner.getSelectedItem();
				TextView t1=(TextView)findViewById(R.id.textView1);
				t1.setText("Spinner Text:"+selected);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mAutoText.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView t2=(TextView)findViewById(R.id.textView2);
				t2.setText("Auto Text:"+mAutoText.getText());
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
