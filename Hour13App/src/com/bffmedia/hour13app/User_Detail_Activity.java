package com.bffmedia.hour13app;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class User_Detail_Activity extends Activity {
	String mode = "edit";//default, add optional
	User_Obj cObj=null;
	EditText user = null;
	EditText pass = null;
	Button Save=null;
	Button Back=null;
	Button Delete=null;
	QdDBHelper cDb=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail);
		//pre init
		mode = getIntent().getExtras().getString("mode");
		if(mode==null || mode.equals(""))
		{
			mode = "edit";
		}
		cDb=new QdDBHelper(this);
		cDb.open();
		//get obj for edit
		if(mode.equals("edit"))
		{
			cObj = cDb.get_obj(getIntent().getExtras().getString("username"));
		}
		else
		{
			cObj=new User_Obj();
		}
		
		
		//get instance and set listenner
		user = (EditText)findViewById(R.id.textView_user);
		pass = (EditText)findViewById(R.id.textView_pass);
		
		Save = (Button)findViewById(R.id.button_Save);
		Save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mode.equals("edit"))
				{
					Save();
				}
				else
				{
					Add();
				}
			}
		});
		Delete = (Button)findViewById(R.id.button_Delete);
		Delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Delete();
			}
		});
		Back = (Button)findViewById(R.id.button_Back);
		Back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Back();
			}
		});
		
		
		//set value
		if(mode.equals("edit"))
		{
			//set value
			user.setText(cObj.username);
			pass.setText(cObj.password);
			//hide
			
			user.setEnabled(false);
			
		}
		else
		{
			//set value
			user.setText("");
			pass.setText("");
			Delete.setVisibility(Button.INVISIBLE);
		}
	}
	private void Add()
	{
		cObj.username= user.getText().toString();
		cObj.password = pass.getText().toString();
		int re = cDb.add(cObj);
		Toast.makeText(this, "Add OK, state = "+re, 1000).show();
	}
	private void Save()
	{
		cObj.username= user.getText().toString();
		cObj.password = pass.getText().toString();
		
		int re = cDb.update(cObj);
		Toast.makeText(this, "Update OK, state = "+re, 1000).show();
	}
	private void Delete()
	{
		cObj.username= user.getText().toString();
		cObj.password = pass.getText().toString();
		int re = cDb.delete(cObj);
		Toast.makeText(this, "Delete OK, state = "+re, 1000).show();
	}
	private void Back()
	{
		super.onBackPressed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user__detail, menu);
		return true;
	}

}
