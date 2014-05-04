package com.qdcatplayer.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.*;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.FileSystem.MyFileChangesInterface;
import com.qdcatplayer.main.FileSystem.MyFolderChanges;
//import com.qdcatplayer.main.GUI.MyLibraryActivity;
import com.qdcatplayer.main.GUI.MainPlayerFragment;
import com.qdcatplayer.main.GUI.MainPlayerFragment.MyMainPLayerDataProvider;
import com.qdcatplayer.main.GUI.MyLibraryActivity;
import com.qdcatplayer.main.Setting.SettingsActivity;


public class MainActivity extends Activity implements MyMainPLayerDataProvider {
	private MediaPlayer mainMediaPlayer=null;
	private MySong currentPlayingSong=null;
	private ArrayList<MySong> songsList=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_layout);
		MyDBManager mn=new MyDBManager();
		mn.getHelper(this).getWritableDatabase();
		//test
		MySongDAO dao=new MySongDAO(this, null);
		dao.setSource(MySource.DB_SOURCE);
		currentPlayingSong= dao.getById(1);
		mainMediaPlayer = new MediaPlayer();
		prepareMediaPlayer(mainMediaPlayer, currentPlayingSong);
		//
		
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab mTab1= actionBar.newTab().setText("List").setTabListener(new KimTabListener());
		Tab mTab2= actionBar.newTab().setText("Music Player").setTabListener(new KimTabListener());
		Tab mTab3= actionBar.newTab().setText("Setting").setTabListener(new KimTabListener());
		actionBar.addTab(mTab1);
		actionBar.addTab(mTab2);
		actionBar.addTab(mTab3);
		
		//LoadToDB();
		
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		/*
		GlobalDAO dao = new GlobalDAO(this);
		dao.setSource(MySource.DB_SOURCE);
		dao.getMySongDAO().getAll();
		dao.getMySongDAO().getHelper().resetDB();
		dao.getMySongDAO().getAll();
		dao.release();
		
		dao=new GlobalDAO(this);
		dao.getMySongDAO().setSource(MySource.DB_SOURCE);
		dao.getMySongDAO().getAll();
		*/
		
		
		//showLibraryActivity();
		
		
		
	}
	/*
	private void update_sample()
	{
		try
		{
			MySongDAO dao=new MySongDAO(this, null);
			dao.setSource(MySource.DB_SOURCE);
			MySong obj = dao.getAll().get(0);
			obj.getAlbum().setName("album3");
			obj.getArtist().setName("artist3");
			obj.setTitle("title3");
			obj.update();
		}catch(Exception e)
		{
			
		}
	}
	*/
	private class KimTabListener implements ActionBar.TabListener{

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if(tab.getText().equals("Music Player")){
				MainPlayerFragment fm=new MainPlayerFragment() {
				};
				FragmentTransaction frt=getFragmentManager().beginTransaction();
				frt.replace(R.id.layout_container, fm);
				frt.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				frt.commit();
			}
			else{
				Fragment fm = new Fragment();
				FragmentTransaction frt=getFragmentManager().beginTransaction();
				frt.replace(R.id.layout_container, fm);
				frt.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				frt.commit();
			}
		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private void showLibraryActivity()
	{
		Intent itt = new Intent(MainActivity.this, MyLibraryActivity.class);
		startActivity(itt);
	}
	
	private void callSetting()
	{
		Intent setting = new Intent(MainActivity.this, SettingsActivity.class);
		startActivity(setting);
	}
	/*
	private void getSongsFromFolderId()
	{
		MyFolder fd=new MyFolder();
		fd.setId(5);
		MyFolderDAO dao=new MyFolderDAO(getApplicationContext(), null);
		dao.setSource(MySource.DB_SOURCE);
		fd.setDao(dao);
		fd.load();
		ArrayList<MySong> ss=fd.getAllRecursiveSongs();
		for(MySong tmp:ss)
		{
			if(!tmp.isOnDisk())
			{
				Log.w("qd", tmp.getPath().getAbsPath());
			}
		}
	}
	*/
	/*
	private void Tracking()
	{
		MyFolderChanges tracker = new MyFolderChanges("/sdcard/music/hay", new MyFileChangesInterface() {
			@Override
			public void onFinish(MyFolderChanges obj) {
				Log.w("qd", "Finish tracking on asynctask, md5="+obj.getMd5());
			}
		});
		tracker.start();
	}
	*/
	/*
	private void LoadToDB()
	{
		//Declare music folder
		MyFolder fd=new MyFolder("/sdcard/music");
		//Init new MyFolderDAO for working with MyFolder Entity
		MyFolderDAO dao = new MyFolderDAO(getApplicationContext(), null);
		dao.setSource(MySource.DISK_SOURCE);
		//Assign DAO to Entity (so far, Entity will pass
		//related-DAO through any deep level FK reference)
		fd.setDao(dao);
		//Get all songs belong to this fd Folder recursively
		ArrayList<MySong> ss = fd.getAllRecursiveSongs();
		//Fetch each song from result
		for(MySong item:ss)
		{
			//call insert on current Entity
			item.insert();//F_Entity will auto insert and keep references
		}
	}
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		callSetting();
		return true;
	}
	@Override
	public MediaPlayer getMediaPlayer() {
		return mainMediaPlayer;
	}
	@Override
	public MySong getCurrentSong() {
		return currentPlayingSong;
	}
	@Override
	public Boolean requestNextSong() {
		Integer cur_index = songsList.indexOf(currentPlayingSong);
		if(cur_index<0)
		{
			return false;
		}
		Integer next_index = cur_index+1;
		if(next_index>=songsList.size())
		{
			return false;
		}
		currentPlayingSong = songsList.get(next_index);
		return true;
	}
	@Override
	public Boolean requestPrevSong() {
		Integer cur_index = songsList.indexOf(currentPlayingSong);
		if(cur_index<0)
		{
			return false;
		}
		Integer prev_index = cur_index-1;
		if(prev_index<0)
		{
			return false;
		}
		currentPlayingSong = songsList.get(prev_index);
		return true;
	}
	private void prepareMediaPlayer(MediaPlayer player, MySong obj)
	{
		if(player==null)
		{
			return;
		}
		try {
			player.reset();
			player.setDataSource(obj.getPath().getAbsPath());
			player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public Boolean setRepeat(Integer mode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean setShuffle(Boolean mode) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
