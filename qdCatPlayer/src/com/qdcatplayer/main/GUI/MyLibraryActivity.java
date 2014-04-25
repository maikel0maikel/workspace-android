package com.qdcatplayer.main.GUI;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.GUI.MyLibraryAlbumsFragment.MyLibraryAlbumItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryArtistsFragment.MyLibraryArtistItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryFoldersFragment.MyLibraryFolderItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryListFragment.MyLibraryClickListener;
import com.qdcatplayer.main.GUI.MyLibrarySongsFragment.MyLibrarySongItemClickListener;
import com.qdcatplayer.main.Setting.FolderChooserPreference;
import com.qdcatplayer.main.Setting.FolderChooserPreference.OnFolderChooserFinishListener;
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
	MyLibraryArtistItemClickListener,
	MyLibraryFolderItemClickListener,
	OnFolderChooserFinishListener,
	_MyLibaryDataProvider
{
	/**
	 * For MyLibaryDataProvider cached
	 */
	private ArrayList<MyAlbum> _albumsProvider=null;
	private ArrayList<MyArtist> _artistsProvider=null;
	private ArrayList<MyFolder> _foldersProvider = null;
	private ArrayList<MySong> _songsProvider=null;
	/**
	 * For All items cached
	 */
	private ArrayList<MyAlbum> _albumsAll=null;
	private ArrayList<MyArtist> _artistsAll=null;
	private ArrayList<MyFolder> _foldersAll = null;
	private ArrayList<MySong> _songsAll=null;
	/**
	 * Shared DAO accross Activity Live
	 */
	private GlobalDAO gDAOs = null;
	public MyLibraryActivity() {
		// TODO Auto-generated constructor stub

	}
	private void callLibraryAlbumsFragment(Boolean addToBackStack, ArrayList<MyAlbum> albums) {
		MyLibraryAlbumsFragment mFragment = new MyLibraryAlbumsFragment();
		_albumsProvider = albums;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.library_main_placeholder, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibraryAlbumsFragment.class.getName());//very importance
        }
        ft.commit();
	}
	private void callLibraryArtistsFragment(Boolean addToBackStack, ArrayList<MyArtist> artists) {
		MyLibraryArtistsFragment mFragment = new MyLibraryArtistsFragment();
		_artistsProvider = artists;
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
	
	private void callLibraryFoldersFragment(Boolean addToBackStack, ArrayList<MyFolder> folders) {
		MyLibraryFoldersFragment mFragment = new MyLibraryFoldersFragment();
		_foldersProvider = folders;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.library_main_placeholder, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibraryFoldersFragment.class.getName());//very importance
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
		//khong nen dung bundle.serialize de pass data, khi saveinstance rat de bi loi
		//set temp song first
		_songsProvider = songs;//very importance
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.library_main_placeholder, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibrarySongsFragment.class.getName());//very importance
        	//Log.w("qd",MyLibrarySongsFragment.class.getName());
        }
        ft.commit();
	}
	@Override
	public ArrayList<MyAlbum> getAlbums() {
		// TODO Auto-generated method stub
		return _albumsProvider;
	}
	@Override
	public ArrayList<MyArtist> getArtists() {
		// TODO Auto-generated method stub
		return _artistsProvider;
	}

	@Override
	public ArrayList<MyFolder> getFolders() {
		// TODO Auto-generated method stub
		return _foldersProvider;
	}

	@Override
	public ArrayList<MySong> getSongs() {
		return _songsProvider;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//Log.w("qd",this.getClass().getName());
		if(data==null)
		{
			return;
		}
		Boolean changed=data.getBooleanExtra(FolderChooserPreference.FOLDER_CHANGED_KEY, false);
		if(changed)
		{
			//backto root GUI first
			callLibraryListFragment();
			//reset all cached member related to DB
			resetDBState();
		}
		return;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_main);
		//init DB DAOs
		resetDBState();
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
	public void OnFolderChooserFinish() {
		// TODO Auto-generated method stub
		Log.w("qd","wtf");
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
	public void onLibraryFolderItemClick(MyFolder current,
			ArrayList<MyFolder> folders) {
		//Display all recursive songs of folder
		callLibrarySongsFragment(true, current.getAllRecursiveSongs());
	}
	@Override
	public void onLibraryItemClick(String itemId) {
		//see R.array.library_item_id_array for values of itemId can be
		if(itemId.toUpperCase().equals("ALLSONGS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(_songsAll==null)
			{
				_songsAll = gDAOs.getMySongDAO().getAll();
			}
			callLibrarySongsFragment(true, _songsAll);
			return;
		}
		else if(itemId.toUpperCase().equals("ALBUMS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(_albumsAll==null)
			{
				_albumsAll = gDAOs.getMyAlbumDAO().getAll();
			}
			callLibraryAlbumsFragment(true, _albumsAll);
			return;
		}
		else if(itemId.toUpperCase().equals("ARTISTS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(_artistsAll==null)
			{
				_artistsAll = gDAOs.getMyArtistDAO().getAll();
			}
			callLibraryArtistsFragment(true, _artistsAll);//do the bi dung do voi MySQLiteHelper.close()
			return;
		}
		else if(itemId.toUpperCase().equals("FOLDERS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(_foldersAll==null)
			{
				_foldersAll = gDAOs.getMyFolderDAO().getAll();
			}
			callLibraryFoldersFragment(true, _foldersAll);//be careful because of cache
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
		//...passing to main player here
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent setting = new Intent(this, SettingsActivity.class);
		startActivityForResult(setting, 1);
		return true;
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//do not call super or error may occur
		//when be in MyLibrarySongsFragment and minimize or switch to other activity
		//may be serialize when passing bundle to fragment from this activity
		//fixed by use another approach
		super.onSaveInstanceState(outState);
	}
	/**
	 * Xoa tat ca cahed, release DB, va sau do tao moi lai
	 */
	private void resetDBState()
	{
		_foldersProvider = null;
		_albumsProvider=null;
		_artistsProvider=null;
		_songsProvider=null;
		
		_foldersAll = null;
		_albumsAll=null;
		_artistsAll=null;
		_songsAll=null;
		
		if(gDAOs!=null)
		{
			gDAOs.release();
			gDAOs=null;
		}
		gDAOs = new GlobalDAO(this);
		gDAOs.setSource(MySource.DB_SOURCE);
	}
	
}
