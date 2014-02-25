package com.example.qd;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setTitle("InputActivity");
        //set onclick
        Button mybt=(Button)findViewById(R.id.my_button);
        mybt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//get value
				EditText so1 = (EditText)findViewById(R.id.editText_so1);
				int so1i = 0;
				try{
					so1i = Integer.parseInt(so1.getText().toString());
				}catch(Exception ex)
				{
					so1i = 0;
				}
				EditText so2 = (EditText)findViewById(R.id.editText_so2);
				int so2i = 0;
				try{
					so2i = Integer.parseInt(so2.getText().toString());
				}catch(Exception ex)
				{
					so2i = 0;
				}
				//create bundle
				
				Bundle bdl=new Bundle();
				bdl.putInt("so1", so1i);
				bdl.putInt("so2", so2i);
				//add to Intent
				Intent intt=new Intent(InputActivity.this, AddActivity.class);
				intt.putExtras(bdl);
				//start activity
				startActivity(intt);
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
