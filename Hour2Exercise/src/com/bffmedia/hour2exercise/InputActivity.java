package com.bffmedia.hour2exercise;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
public class InputActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input);
		final EditText num1=(EditText)findViewById(R.id.numEditText1);
		final EditText num2=(EditText)findViewById(R.id.numEditText2);
		Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle=new Bundle();
				bundle.putInt("num1", Integer.parseInt(num1.getText().toString()));
				bundle.putInt("num2", Integer.parseInt(num2.getText().toString()));
				Intent intent=new Intent(InputActivity.this,AddActivity.class);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input, menu);
		return true;
	}

}
