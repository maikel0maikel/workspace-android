package com.qdcatplayer.main.GUI;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.GUI.MyLibraryAlbumsFragment.MyLibraryAlbumItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryArtistsFragment.MyLibraryArtistItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryListFragment.MyLibraryClickListener;
import com.qdcatplayer.main.GUI.MyLibrarySongsFragment.MyLibrarySongItemClickListener;
import com.qdcatplayer.main.Setting.SettingsActivity;
/**
 * Library main/root controller
 * @author admin
 *
 */
public class MyLibraryActivity
extends
	Activity
implements
	MyLibraryClickListener,
	MyLibrarySongItemClickListener,
	MyLibraryAlbumItemClickListener,
	MyLibraryArtistItemClickListener
{
	/**
	 * Shared DAO accross Activity Live
	 */
	private GlobalDAO gDAOs = null;
	private void callLibraryAlbumsFragment(Boolean addToBackStack, ArrayList<MyAlbum> albums) {
		MyLibraryAlbumsFragment mFragment = new MyLibraryAlbumsFragment();
		Bundle bundle = new Bundle();
		//set data
		bundle.putSerializable(MyLibraryAlbumsFragment.ALBUMS, albums);
		//set arg
		mFragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.library_main_placeholder, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack("MyLibraryAlbumsFragment");//very importance
        }
        ft.commit();
	}
	
	private void callLibraryArtistsFragment(Boolean addToBackStack, ArrayList<MyArtist> artists) {
		MyLibraryArtistsFragment mFragment = new MyLibraryArtistsFragment();
		Bundle bundle = new Bundle();
		//set data
		bundle.putSerializable(MyLibraryArtistsFragment.ARTISTS, artists);
		//set arg
		mFragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.library_main_placeholder, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibraryArtistsFragment.class.getName());//very importance
        }
        ft.commit();
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
	/**
	 * 
	 * @param addToBackStack
	 * @param songs Songs want to display in Fragment
	 */
	private void callLibrarySongsFragment(Boolean addToBackStack, ArrayList<MySong> songs)
	{
		MyLibrarySongsFragment mFragment = new MyLibrarySongsFragment();
		Bundle bundle = new Bundle();
		//set data
		bundle.putSerializable(MyLibrarySongsFragment.SONGS, songs);
		//set arg
		mFragment.setArguments(bundle);
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_main);
		//init DB DAOs
		gDAOs = new GlobalDAO(getApplicationContext());
		gDAOs.setSource(MySource.DB_SOURCE);//only work on DB SOURCE
		//View by default
		callLibraryListFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLibraryAlbumItemClick(MyAlbum current,
			ArrayList<MyAlbum> albums) {
		//Display songs of album
		callLibrarySongsFragment(true, current.getSongs());
	}
	@Override
	public void onLibraryArtistItemClick(MyArtist current,
			ArrayList<MyArtist> artists) {
		//Display songs of artist
		callLibrarySongsFragment(true, current.getSongs());
	}

	@Override
	public void onLibraryItemClick(String itemId) {
		//see R.array.library_item_id_array for values of itemId can be
		if(itemId.toUpperCase().equals("ALLSONGS_ITEM"))
		{
			callLibrarySongsFragment(true, gDAOs.getMySongDAO().getAll());
			return;
		}
		else if(itemId.toUpperCase().equals("ALBUMS_ITEM"))
		{
			callLibraryAlbumsFragment(true, gDAOs.getMyAlbumDAO().getAll());
			return;
		}
		else if(itemId.toUpperCase().equals("ARTISTS_ITEM"))
		{
			callLibraryArtistsFragment(true, gDAOs.getMyArtistDAO().getAll());
			return;
		}
		//default
		else
		{
			callLibraryListFragment();
			return;
		}
		
	}

	@Override
	public void onLibrarySongItemClick(MySong current,
			ArrayList<MySong> playlist) {
		Toast.makeText(this, current.getPath().getAbsPath()+":"+playlist.size(), 200).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent setting = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(setting);
		return true;
	}
	
}
