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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MyPlayList;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.FileSystem.MyFileChangesInterface;
import com.qdcatplayer.main.FileSystem.MyFolderChanges;
//import com.qdcatplayer.main.GUI.MyLibraryActivity;
import com.qdcatplayer.main.GUI.MainPlayerFragment;
import com.qdcatplayer.main.GUI.MyLibraryAlbumsFragment;
import com.qdcatplayer.main.GUI.MyLibraryArtistsFragment;
import com.qdcatplayer.main.GUI.MyLibraryEnqueueFragment;
import com.qdcatplayer.main.GUI.MyLibraryFoldersFragment;
import com.qdcatplayer.main.GUI.MyLibraryListFragment;
import com.qdcatplayer.main.GUI.MyLibraryPlayListsFragment;
import com.qdcatplayer.main.GUI.MyLibrarySongsFragment;
import com.qdcatplayer.main.GUI._MyLibaryDataProvider;
import com.qdcatplayer.main.GUI.CtxDialog.MyLibrarySongsCtxDialog;
import com.qdcatplayer.main.GUI.MainPlayerFragment.MyMainPLayerDataProvider;
import com.qdcatplayer.main.GUI.MyLibraryAlbumsFragment.MyLibraryAlbumItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryArtistsFragment.MyLibraryArtistItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryFoldersFragment.MyLibraryFolderItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryListFragment.MyLibraryClickListener;
import com.qdcatplayer.main.GUI.MyLibraryPlayListsFragment.MyLibraryPlayListItemClickListener;
import com.qdcatplayer.main.GUI.MyLibrarySongsFragment.MyLibrarySongItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryActivity_BAK;
import com.qdcatplayer.main.Setting.FolderChooserPreference;
import com.qdcatplayer.main.Setting.SettingsActivity;
import com.qdcatplayer.main.Setting.FolderChooserPreference.OnFolderChooserFinishListener;
import com.qdcatplayer.main.SharedAdapter.MyLibrarySongsAdapter;


public class MainActivity extends Activity
implements
/**
 * =============================
 * Main Player area
 */
MyMainPLayerDataProvider,
/**
 * =============================
 * Library area
 */
MyLibraryClickListener,
MyLibrarySongItemClickListener,
MyLibraryAlbumItemClickListener,
MyLibraryArtistItemClickListener,
MyLibraryFolderItemClickListener,
MyLibraryPlayListItemClickListener,
_MyLibaryDataProvider
{
	private int layout_container = R.id.layout_container;
	private class Player
	{
		public Player()
		{
			
		}
		/**
		 * For MainPlayer Fragment
		 */
		public Random r_tmp = null;
		public MediaPlayer mainMediaPlayer=null;
		public MySong currentPlayingSong=null;
		public ArrayList<MySong> songsList=null;
		public Boolean shuffleMode=false;
		public Integer repeatMode=0;
		public HashMap<Integer, MySong> playedList = null;
		public Stack<MySong> playedStack = null;
		/**
		 * End
		 */
	}
	private ActionBar actionBar=null;
	private Player PL = new Player();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		//Step 1:prepare DB
		MyDBManager mn=new MyDBManager();
		mn.getHelper(this).getWritableDatabase();
		//Step 2:init
		InitMyLibrary();
		//Step 2:init main player
		MySongDAO dao=new MySongDAO(this, null);
		dao.setSource(MySource.DB_SOURCE);
		PL.songsList = dao.getAll();
		PL.currentPlayingSong= PL.songsList.get(0);
		PL.mainMediaPlayer = new MediaPlayer();
		prepareMediaPlayer(PL.mainMediaPlayer, PL.currentPlayingSong);
		PL.playedList = new HashMap<Integer, MySong>();
		PL.playedList.put(PL.songsList.indexOf(PL.currentPlayingSong), PL.currentPlayingSong);
		PL.playedStack = new Stack<MySong>();
		PL.playedStack.push(PL.currentPlayingSong);
		PL.r_tmp = new Random();
		
		//Step 3: Init tab
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab mTab1= actionBar.newTab().setText("library").setTabListener(new KimTabListener());
		Tab mTab2= actionBar.newTab().setText("main_player").setTabListener(new KimTabListener());
		Tab mTab3= actionBar.newTab().setText("list").setTabListener(new KimTabListener());
		
		actionBar.addTab(mTab1);
		actionBar.addTab(mTab2);
		actionBar.addTab(mTab3);
		//Step 4: default view
		//callLibraryListFragment();
	}
	private void clearAllBackStack()
	{
		FragmentManager fm = getFragmentManager();
		for(int i = 0; i < fm.getBackStackEntryCount(); i++) {    
		    fm.popBackStack();
		}
	}
	private class KimTabListener implements ActionBar.TabListener{

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction arg1) {
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if(tab.getText().equals("main_player")){
				//Step 2
				callMainPlayerFragment(false);
				//khong can dang ky listener khi choi xong
				//ben trong fragment no tu dang ky roi
				
			}else if(tab.getText().equals("library")){
				callLibraryListFragment(false);
				//vi khi thoat fragment main player thi mat listener
				//begin: register listener for update next song auto
		  		PL.mainMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
		  			@Override
		  			public void onCompletion(MediaPlayer mp) {
		  				if(requestNextSong())
		  				{
		  					PL.mainMediaPlayer.start();
		  				}
		  			}
		  		});
		  		//end
				
			}else if(tab.getText().equals("list")){
				callLibraryEnqueueFragment(false, LI._enqueue);
				//vi khi thoat fragment main player thi mat listener
				//begin: register listener for update next song auto
		  		PL.mainMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
		  			@Override
		  			public void onCompletion(MediaPlayer mp) {
		  				if(requestNextSong())
		  				{
		  					PL.mainMediaPlayer.start();
		  				}
		  			}
		  		});
		  		//end
			}
		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
			
		}
		
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
	private void callMainPlayerFragment(Boolean addToBackStack)
	{
		MainPlayerFragment fm=new MainPlayerFragment() {
		};
		FragmentTransaction frt=getFragmentManager().beginTransaction();
		frt.replace(layout_container, fm);
		frt.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		if(addToBackStack)
		{
			frt.addToBackStack(MainPlayerFragment.class.getName());
		}
		frt.commit();
		//switch tab
		actionBar.selectTab(actionBar.getTabAt(1));
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
	public MediaPlayer getMediaPlayer() {
		return PL.mainMediaPlayer;
	}
	@Override
	public MySong getCurrentSong() {
		return PL.currentPlayingSong;
	}
	@Override
	public Boolean requestNextSong() {
		Integer index = PL.songsList.indexOf(PL.currentPlayingSong);
		//Step 1: Calculate next index
		//check repeate mode
		if(PL.repeatMode==0)
		{
			//check shuffle mode
			if(PL.shuffleMode)
			{
				if(PL.playedList.size()<PL.songsList.size())
				{
					while(true)
					{
						index = PL.r_tmp.nextInt(PL.songsList.size());
						if(!PL.playedList.containsKey(index))
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
				if(index>=PL.songsList.size())
				{
					index=-1;
				}
			}
		}
		else if(PL.repeatMode==1)
		{
			
		} else if(PL.repeatMode==2)
		{
			if(PL.shuffleMode)
			{
				if(PL.playedList.size()>=PL.songsList.size())
				{
					PL.playedList.clear();
					//playedStack.clear(); to reduce memory
				}
				
				while(true)
				{
					index = PL.r_tmp.nextInt(PL.songsList.size());
					if(!PL.playedList.containsKey(index))
					{
						break;
					}
				}
			}
			else
			{
				index++;
				if(index>=PL.songsList.size())
				{
					index=0;
				}
			}
		}
		//final
		if(index>=0)
		{
			PL.currentPlayingSong = PL.songsList.get(index);
			PL.playedStack.push(PL.currentPlayingSong);
			PL.playedList.put(index, PL.currentPlayingSong);
			return prepareMediaPlayer(PL.mainMediaPlayer, PL.currentPlayingSong);
		}
		return false;
	}
	@Override
	public Boolean requestPrevSong() {
		if(PL.repeatMode==1)
		{
			try{
				return prepareMediaPlayer(PL.mainMediaPlayer, PL.currentPlayingSong);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(PL.playedStack.size()>=2)
		{
			try{
				PL.playedList.remove(PL.songsList.indexOf(PL.playedStack.pop()));
				PL.currentPlayingSong = PL.playedStack.lastElement();
				return prepareMediaPlayer(PL.mainMediaPlayer, PL.currentPlayingSong);
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
	@Override
	public Boolean prepareMediaPlayer(MediaPlayer player, MySong obj)
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
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public Boolean setRepeat(Integer mode) {
		PL.repeatMode = mode;
		return true;
	}
	@Override
	public Boolean setShuffle(Boolean mode) {
		PL.shuffleMode = mode;
		return true;
	}

	@Override
	public Boolean getShuffle() {
		return PL.shuffleMode;
	}

	@Override
	public Integer getRepeat() {
		return PL.repeatMode;
	}

	@Override
	public Integer getPLayedCount() {
		return PL.playedList.size();
	}

	@Override
	public Integer getTotalCount() {
		return PL.songsList.size();
	}
	/**
	 * add to PL.songList
	 * @param obj
	 */
	public void addToSongList(MySong obj)
	{
		if(!obj.isSongInList(LI._enqueue))
		{
			LI._enqueue.add(obj);
		}
		//move enqueue to PL list
		PL.songsList = LI._enqueue;
	}
	
	/**
	 * ===============================================================
	 * Khu vuc cua Library
	 */
	private class Library
	{
		
		/**
		 * For MyLibaryDataProvider cached, Fragment use
		 */
		public ArrayList<MyAlbum> _albumsProvider=null;
		public ArrayList<MyArtist> _artistsProvider=null;
		public ArrayList<MyFolder> _foldersProvider = null;
		public ArrayList<MySong> _songsProvider=null;
		public ArrayList<MyPlayList> _playListsProvider=null;
		/**
		 * For All items cached
		 */
		public ArrayList<MyAlbum> _albumsAll=null;
		public ArrayList<MyArtist> _artistsAll=null;
		public ArrayList<MyFolder> _foldersAll = null;
		public ArrayList<MySong> _songsAll=null;
		public ArrayList<MyPlayList> _playListsAll=null;
		/**
		 * Shared DAO accross Activity Live
		 */
		public GlobalDAO _gDAOs = null;
		/**
		 * Enqueue
		 */
		public ArrayList<MySong> _enqueue = null;
	}
	private Library LI = new Library();
	/**
	 * 
	 */
	private void callLibraryAlbumsFragment(Boolean addToBackStack, ArrayList<MyAlbum> albums) {
		MyLibraryAlbumsFragment mFragment = new MyLibraryAlbumsFragment();
		LI._albumsProvider = albums;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(layout_container, mFragment);
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
		LI._artistsProvider = artists;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(layout_container, mFragment);
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
		LI._foldersProvider = folders;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(layout_container, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibraryFoldersFragment.class.getName());//very importance
        }
        ft.commit();
	}
	private void callLibraryListFragment(Boolean addToBackStack)
	{
		MyLibraryListFragment mFragment = new MyLibraryListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(layout_container, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibraryListFragment.class.getName());//very importance
        }
        ft.commit();
        //switch tab
  		actionBar.selectTab(actionBar.getTabAt(0));
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
		LI._songsProvider = songs;//very importance
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(layout_container, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibrarySongsFragment.class.getName());//very importance
        	//Log.w("qd",MyLibrarySongsFragment.class.getName());
        }
        ft.commit();
        
	}
	private void callLibraryPlayListsFragment(Boolean addToBackStack, ArrayList<MyPlayList> playLists)
	{
		MyLibraryPlayListsFragment mFragment = new MyLibraryPlayListsFragment();
		//khong nen dung bundle.serialize de pass data, khi saveinstance rat de bi loi
		//set temp song first
		LI._playListsProvider = playLists;//very importance
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(layout_container, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibraryPlayListsFragment.class.getName());//very importance
        	//Log.w("qd",MyLibrarySongsFragment.class.getName());
        }
        ft.commit();
	}
	private void callLibraryEnqueueFragment(Boolean addToBackStack, ArrayList<MySong> playLists)
	{
		MyLibraryEnqueueFragment mFragment = new MyLibraryEnqueueFragment();
		//khong nen dung bundle.serialize de pass data, khi saveinstance rat de bi loi
		//set temp song first
		LI._enqueue = playLists;//very importance
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(layout_container, mFragment);
        //since addToBackStacl allow user to navigate to blank screen
        //when app not swap fragment yet
        if(addToBackStack)
        {
        	ft.addToBackStack(MyLibraryEnqueueFragment.class.getName());//very importance
        	//Log.w("qd",MyLibrarySongsFragment.class.getName());
        }
        ft.commit();
        //switch tab
  		//actionBar.selectTab(actionBar.getTabAt(2));//layout can error
	}
	@Override
	public ArrayList<MyAlbum> getAlbums() {
		return LI._albumsProvider;
	}
	@Override
	public ArrayList<MyArtist> getArtists() {
		return LI._artistsProvider;
	}

	@Override
	public ArrayList<MyFolder> getFolders() {
		return LI._foldersProvider;
	}

	@Override
	public ArrayList<MySong> getSongs() {
		return LI._songsProvider;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
			callLibraryListFragment(false);
			//switch tab
			actionBar.selectTab(actionBar.getTabAt(0));
			//reset all cached member related to DB
			resetLibraryDBState();
		}
		return;
	}

	
	protected void InitMyLibrary() {
		//init DB DAOs
		resetLibraryDBState();
		//init memeber pre-values
		LI._enqueue = new ArrayList<MySong>();
		//View by default
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
	public void onLibraryFolderItemClick(MyFolder current,
			ArrayList<MyFolder> folders) {
		//Display all recursive songs of folder
		callLibrarySongsFragment(true, current.getAllRecursiveSongs());
	}
	@Override
	public void onLibraryPlayListItemClick(MyPlayList current,
			ArrayList<MyPlayList> playlists) {
		//Display all recursive songs of playList
		callLibrarySongsFragment(true, current.getSongs());
	}
	@Override
	public void onLibraryItemClick(String itemId) {
		//see R.array.library_item_id_array for values of itemId can be
		if(itemId.toUpperCase().equals("ALLSONGS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(LI._songsAll==null)
			{
				LI._songsAll = LI._gDAOs.getMySongDAO().getAll();
			}
			callLibrarySongsFragment(true, LI._songsAll);
			return;
		}
		else if(itemId.toUpperCase().equals("ALBUMS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(LI._albumsAll==null)
			{
				LI._albumsAll = LI._gDAOs.getMyAlbumDAO().getAll();
			}
			callLibraryAlbumsFragment(true, LI._albumsAll);
			return;
		}
		else if(itemId.toUpperCase().equals("ARTISTS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(LI._artistsAll==null)
			{
				LI._artistsAll = LI._gDAOs.getMyArtistDAO().getAll();
			}
			callLibraryArtistsFragment(true, LI._artistsAll);//do the bi dung do voi MySQLiteHelper.close()
			return;
		}
		else if(itemId.toUpperCase().equals("FOLDERS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(LI._foldersAll==null)
			{
				LI._foldersAll = LI._gDAOs.getMyFolderDAO().getAll();
			}
			callLibraryFoldersFragment(true, LI._foldersAll);//be careful because of cache
			return;
		}
		else if(itemId.toUpperCase().equals("PLAYLISTS_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(LI._playListsAll==null)
			{
				LI._playListsAll = LI._gDAOs.getMyPlayListDAO().getAll();
			}
			callLibraryPlayListsFragment(true, LI._playListsAll);//be careful because of cache
			return;
		}
		else if(itemId.toUpperCase().equals("ENQUEUE_ITEM"))
		{
			//cached because of time to load allrecursive song for count
			if(LI._enqueue==null)
			{
				LI._enqueue = new ArrayList<MySong>();
			}
			callLibraryEnqueueFragment(true, LI._enqueue);//be careful because of cache
			return;
		}
		//default
		else
		{
			callLibraryListFragment(true);
			return;
		}
	}
	/**
	 * Dung de add vo enqueue, player list, va tu dong choi
	 */
	@Override
	public void onLibrarySongItemClick(MySong current,
			ArrayList<MySong> playlist) {
		//Toast.makeText(this, current.getPath().getAbsPath()+":"+playlist.size(), 200).show();
		//set enqueue
		LI._enqueue = playlist;
		//set player list from enqueue
		PL.songsList = LI._enqueue;
		PL.currentPlayingSong = current;
		//reset played and stack
		PL.playedList = new HashMap<Integer, MySong>();
		PL.playedStack = new Stack<MySong>();
		PL.playedList.put(PL.songsList.indexOf(PL.currentPlayingSong), PL.currentPlayingSong);
		prepareMediaPlayer(PL.mainMediaPlayer, PL.currentPlayingSong);
		PL.mainMediaPlayer.start();//force to play right now
		//call fragment main player
		callMainPlayerFragment(true);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
	private void resetLibraryDBState()
	{
		LI._foldersProvider = null;
		LI._albumsProvider=null;
		LI._artistsProvider=null;
		LI._songsProvider=null;
		LI._playListsProvider=null;
		
		LI._foldersAll = null;
		LI._albumsAll=null;
		LI._artistsAll=null;
		LI._songsAll=null;
		LI._playListsAll=null;
		LI._enqueue = null;
		//PLAYER
		
		
		if(LI._gDAOs!=null)
		{
			LI._gDAOs.release();
			LI._gDAOs=null;
		}
		LI._gDAOs = new GlobalDAO(this);
		LI._gDAOs.setSource(MySource.DB_SOURCE);
	}
	@Override
	public ArrayList<MyPlayList> getPlayLists() {
		return LI._playListsProvider;
	}
	MyLibrarySongsCtxDialog songs_ctx_dialog = null;
	@Override
	public void onLibrarySongItemLongClick(MySong current,
			ArrayList<MySong> songs) {
		//Toast.makeText(this, "onLibrarySongItemLongClick", 300).show();
		songs_ctx_dialog = new MyLibrarySongsCtxDialog(this, current, new MyLibrarySongsCtxDialog.MyLibrarySongsCtxItemListener() {
			
			@Override
			public void OnLibrarySongsCtxClick_EDIT_TAG(MySong obj) {
				Log.w("qd", "edit tag clicked"+getClass().getName());
				addToSongList(obj);
				songs_ctx_dialog.dismiss();
			}
			
			@Override
			public void OnLibrarySongsCtxClick_ADD_TO_PLAYLIST(MySong obj) {
				Log.w("qd", "add to playlist clicked"+getClass().getName());
				songs_ctx_dialog.dismiss();
			}
			
			@Override
			public void OnLibrarySongsCtxClick_ADD_TO_ENQUEUE(MySong obj) {
				Log.w("qd", "add to enqueue clicked"+getClass().getName());
				addToSongList(obj);
				songs_ctx_dialog.dismiss();
			}
		});
		songs_ctx_dialog.setTitle("Context dialog");
		songs_ctx_dialog.show();
	}
	@Override
	public ArrayList<MySong> getEnqueue() {
		return LI._enqueue;
	}
	
}
