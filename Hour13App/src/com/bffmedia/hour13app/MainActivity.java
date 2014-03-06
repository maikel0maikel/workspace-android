package com.bffmedia.hour13app;
import java.io.IOException;

import com.bffmedia.hour13app.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button Add;
	
	private void Add_()
	{
		Intent intt=new Intent(this,User_Detail_Activity.class);
		intt.putExtra("mode", "add");
		startActivity(intt);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Add = (Button)findViewById(R.id.button_add);
		Add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Add_();
				
			}
		});
		
		showList();
		//qd
		/*QdDBHelper kkk=new QdDBHelper(this);
		try {
			kkk.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showList();
	}
	public void showList(){
		DSUSERList fragmentA = new DSUSERList();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.layout_container, fragmentA);
		ft.addToBackStack("fragment a");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
