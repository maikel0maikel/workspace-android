package com.example.hour7app;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.item1:
	            Toast.makeText(MainActivity.this, "Action1 is pointed",3000).show();
	            return true;
	        case R.id.item2:
	        	Toast.makeText(MainActivity.this, "Action2 is pointed",3000).show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
