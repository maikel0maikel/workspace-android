package com.qdcatplayer.main.GUI;

import java.util.ArrayList;

import com.qdcatplayer.main.MainActivity;
import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.GUI.MyLibraryListFragment.MyLibraryClickListener;
import com.qdcatplayer.main.GUI.MyLibrarySongsFragment.MyLibrarySongItemClickListener;
import com.qdcatplayer.main.R.layout;
import com.qdcatplayer.main.R.menu;
import com.qdcatplayer.main.Setting.SettingsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MyLibraryActivity
extends
	Activity
implements
	MyLibraryClickListener,
	MyLibrarySongItemClickListener
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_main);
		callLibraryListFragment();
		//callLibrarySongsFragment();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent setting = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(setting);
		return true;
	}
	private void callLibraryListFragment()
	{
		MyLibraryListFragment mFragment = new MyLibraryListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.library_main_placeholder, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        //ft.addToBackStack("MyLibraryListFragment");//very importance
        
        ft.commit();
	}
	private void callLibrarySongsFragment(Boolean addToBackStack)
	{
		MyLibrarySongsFragment mFragment = new MyLibrarySongsFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.library_main_placeholder, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack("MyLibrarySongsFragment");//very importance
        }
        ft.commit();
	}
	@Override
	public void onLibraryItemClick(String itemId) {
		if(itemId.toUpperCase().equals("ALLSONGS_ITEM"))
		{
			callLibrarySongsFragment(true);
			return;
		}
		callLibraryListFragment();
	}

	@Override
	public void onLibrarySongItemClick(MySong current,
			ArrayList<MySong> playlist) {
		Toast.makeText(this, current.getPath().getAbsPath()+":"+playlist.size(), 200).show();
	}

}
