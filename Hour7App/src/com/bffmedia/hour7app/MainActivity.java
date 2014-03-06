package com.bffmedia.hour7app;

import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Choreographer.FrameCallback;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	NavigationListener mNavigationListener = new NavigationListener();
	String[]values={"one","one hundred"};
	ArrayAdapter<String>  spinnerAdapter;
	Tab mTab1;
	Tab mTab2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	//	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(actionBar.getNavigationMode()==ActionBar.NAVIGATION_MODE_LIST)
				{
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
					Toast toast=Toast.makeText(getApplicationContext(), "Tab mode", Toast.LENGTH_SHORT);
					toast.show();
					actionBar.removeAllTabs();
					for(int i=0;i<values.length;i++){
						Tab t=actionBar.newTab().setText(values[i]).setTabListener(new ExampleTabListener());
						actionBar.addTab(t);
					}
				}
				else if(actionBar.getNavigationMode()==ActionBar.NAVIGATION_MODE_TABS)
				{
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
					Toast toast=Toast.makeText(getApplicationContext(), "List mode", Toast.LENGTH_SHORT);
					toast.show();
					spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.navigation_item, values); 
					actionBar.setListNavigationCallbacks(spinnerAdapter, mNavigationListener);
				}
			}
		});
//		mTab1= actionBar.newTab().setText("Music Player").setTabListener(new ExampleTabListener());
//		mTab2= actionBar.newTab().setText("List").setTabListener(new ExampleTabListener());
//		actionBar.addTab(mTab1);
//		actionBar.addTab(mTab2);
		
		
	}
private class ExampleTabListener implements ActionBar.TabListener{

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Toast toast=Toast.makeText(getApplicationContext(), "tab", Toast.LENGTH_SHORT);
		if(tab.getText().equals("one")){
			
			toast.setText("First tab");
			toast.show();
		}
		else{
			
			toast.setText("The others");
			toast.show();
		}
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Toast toast=Toast.makeText(getApplicationContext(), "Option 1", Toast.LENGTH_SHORT);
		switch(item.getItemId()){
		case R.id.menu_test:
			toast.setText("Item 1 - action 1");
			toast.show();
			return true;
		case R.id.menu_test2:
			toast.setText("Item 2 - action 2");
			toast.show();
			return true;
		case R.id.menu_test1:
			toast.setText("Item 3");
			toast.show();
			return true;
		case R.id.action_settings:
			toast.setText("setting 1");
			toast.show();
			return true;
		case R.id.action_settings1:
			toast.setText("Item 4");
			toast.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}		
	}
private class NavigationListener implements ActionBar.OnNavigationListener{
	@Override
	public boolean onNavigationItemSelected(int position,long itemID){
		String selected=values[position];
		Toast toast=Toast.makeText(getApplicationContext(),selected,Toast.LENGTH_SHORT);
		toast.show();
		return false;
	}
}
}
