package com.qdcatplayer.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Stack;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.*;

import android.R.integer;
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
import com.qdcatplayer.main.Setting.FolderChooserPreference.OnFolderChooserFinishListener;


public class MainActivity extends Activity
implements
MyMainPLayerDataProvider
{
	/**
	 * For MainPlayer Fragment
	 */
	private Random r_tmp = null;
	private MediaPlayer mainMediaPlayer=null;
	private MySong currentPlayingSong=null;
	private ArrayList<MySong> songsList=null;
	private Boolean shuffleMode=false;
	private Integer repeatMode=0;
	private HashMap<Integer, MySong> playedList = null;
	private Stack<MySong> playedStack = null;
	/**
	 * End
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		//prepare DB
		MyDBManager mn=new MyDBManager();
		mn.getHelper(this).getWritableDatabase();
		//prepare media provider for test
		MySongDAO dao=new MySongDAO(this, null);
		dao.setSource(MySource.DB_SOURCE);
		songsList = dao.getAll();
		currentPlayingSong= songsList.get(0);
		mainMediaPlayer = new MediaPlayer();
		prepareMediaPlayer(mainMediaPlayer, currentPlayingSong);
		playedList = new HashMap<Integer, MySong>();
		playedList.put(songsList.indexOf(currentPlayingSong), currentPlayingSong);
		playedStack = new Stack<MySong>();
		playedStack.push(currentPlayingSong);
		r_tmp = new Random();
		//
		
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab mTab1= actionBar.newTab().setText("List").setTabListener(new KimTabListener());
		Tab mTab2= actionBar.newTab().setText("Music Player").setTabListener(new KimTabListener());
		Tab mTab3= actionBar.newTab().setText("Setting").setTabListener(new KimTabListener());
		actionBar.addTab(mTab1);
		actionBar.addTab(mTab2);
		actionBar.addTab(mTab3);
		
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
		startActivityForResult(setting, 1);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
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
		Integer index = songsList.indexOf(currentPlayingSong);
		//Step 1: Calculate next index
		//check repeate mode
		if(repeatMode==0)
		{
			//check shuffle mode
			if(shuffleMode)
			{
				if(playedList.size()<songsList.size())
				{
					while(true)
					{
						index = r_tmp.nextInt(songsList.size());
						if(!playedList.containsKey(index))
						{
							break;
						}
					}
				}
				else
				{
					index=-1;
				}
			}
			else
			{
				index++;
				if(index>=songsList.size())
				{
					index=-1;
				}
			}
		}
		else if(repeatMode==1)
		{
			
		} else if(repeatMode==2)
		{
			if(shuffleMode)
			{
				if(playedList.size()>=songsList.size())
				{
					playedList.clear();
					//playedStack.clear(); to reduce memory
				}
				
				while(true)
				{
					index = r_tmp.nextInt(songsList.size());
					if(!playedList.containsKey(index))
					{
						break;
					}
				}
			}
			else
			{
				index++;
				if(index>=songsList.size())
				{
					index=0;
				}
			}
		}
		//final
		if(index>=0)
		{
			currentPlayingSong = songsList.get(index);
			playedStack.push(currentPlayingSong);
			playedList.put(index, currentPlayingSong);
			return prepareMediaPlayer(mainMediaPlayer, currentPlayingSong);
		}
		return false;
	}
	@Override
	public Boolean requestPrevSong() {
		if(playedStack.size()>=2)
		{
			try{
				playedList.remove(songsList.indexOf(playedStack.pop()));
				currentPlayingSong = playedStack.lastElement();
				return prepareMediaPlayer(mainMediaPlayer, currentPlayingSong);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * final method to get media player ready before fragment can call
	 * @param player
	 * @param obj
	 * @return
	 */
	private Boolean prepareMediaPlayer(MediaPlayer player, MySong obj)
	{
		if(player==null || obj==null)
		{
			return false;
		}
		try {
			player.reset();
			player.setDataSource(obj.getPath().getAbsPath());
			player.prepare();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public Boolean setRepeat(Integer mode) {
		repeatMode = mode;
		return true;
	}
	@Override
	public Boolean setShuffle(Boolean mode) {
		shuffleMode = mode;
		return true;
	}

	@Override
	public Boolean getShuffle() {
		return shuffleMode;
	}

	@Override
	public Integer getRepeat() {
		return repeatMode;
	}

	@Override
	public Integer getPLayedCount() {
		return playedList.size();
	}

	@Override
	public Integer getTotalCount() {
		return songsList.size();
	}
	
}
