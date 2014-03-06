package com.bffmedia.hour6app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Bundle;
import android.R.anim;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Excercise extends Activity {
	ArrayList<String> values=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise);
		final AutoCompleteTextView autoT=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		Button addButton=(Button)findViewById(R.id.button1);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText t=(EditText)findViewById(R.id.editText1);
				String item=t.getText().toString();
				values.add(item);
				ArrayAdapter<String> autoTextAdapter=new ArrayAdapter<String>(Excercise.this, android.R.layout.simple_spinner_dropdown_item, values);
				autoT.setAdapter(autoTextAdapter);
				t.setText("");
				Toast.makeText(Excercise.this, "Them thanh cong", Toast.LENGTH_SHORT).show();
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
