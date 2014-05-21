package com.qdcatplayer.main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;

import org.apache.http.util.LangUtils;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.qdcatplayer.main.BackgroundTasks.MyPlayer;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.DAOs._GlobalDAOInterface;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MyPlayList;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.GUI.MainPlayerFragment;
import com.qdcatplayer.main.GUI.MainPlayerFragment.MyMainPLayerDataProvider;
import com.qdcatplayer.main.GUI.MyLibraryAlbumsFragment;
import com.qdcatplayer.main.GUI.MyLibraryAlbumsFragment.MyLibraryAlbumItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryArtistsFragment;
import com.qdcatplayer.main.GUI.MyLibraryArtistsFragment.MyLibraryArtistItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryEnqueueFragment;
import com.qdcatplayer.main.GUI.MyLibraryFoldersFragment;
import com.qdcatplayer.main.GUI.MyLibraryFoldersFragment.MyLibraryFolderItemClickListener;
import com.qdcatplayer.main.GUI.MyLibraryListFragment;
import com.qdcatplayer.main.GUI.MyLibraryListFragment.MyLibraryClickListener;
import com.qdcatplayer.main.GUI.MyLibraryPlayListsFragment;
import com.qdcatplayer.main.GUI.MyLibraryPlayListsFragment.MyLibraryPlayListItemClickListener;
import com.qdcatplayer.main.GUI.MyLibrarySongsFragment;
import com.qdcatplayer.main.GUI.MyLibrarySongsFragment.MyLibrarySongItemClickListener;
import com.qdcatplayer.main.GUI._MyLibaryDataProvider;
import com.qdcatplayer.main.GUI.CtxDialog.EditTagDialog;
import com.qdcatplayer.main.GUI.CtxDialog.EditTagDialog.EditDialogListener;
import com.qdcatplayer.main.GUI.CtxDialog.MyLibraryFoldersCtxDialog;
import com.qdcatplayer.main.GUI.CtxDialog.MyLibrarySongsCtxDialog;
import com.qdcatplayer.main.Setting.FolderChooserPreference;
import com.qdcatplayer.main.Setting.SettingsActivity;

//import com.qdcatplayer.main.GUI.MyLibraryActivity;

public class MainActivity extends Activity implements
/**
 * ============================= Main Player area
 */
MyMainPLayerDataProvider,
/**
 * ============================= Library area
 */
MyLibraryClickListener, MyLibrarySongItemClickListener,
		MyLibraryAlbumItemClickListener, MyLibraryArtistItemClickListener,
		MyLibraryFolderItemClickListener, MyLibraryPlayListItemClickListener,
		EditDialogListener, _MyLibaryDataProvider {
	private int layout_container = R.id.layout_container;

	

	private ActionBar actionBar = null;
	private MyPlayer PL = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.main_layout);
		
		//load language
		loadLanguage();
		// Step 1:prepare DB
		MyDBManager mn = new MyDBManager();
		mn.getHelper(this).getWritableDatabase();
		// Step 2:init
		InitMyLibrary();
		// Step 2:init main player
		MySongDAO dao = new MySongDAO(this, null);
		dao.setSource(MySource.DB_SOURCE);
		PL = new MyPlayer(null, null);
		// Step 3: Init tab
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab mTab1 = actionBar.newTab().setText("library")
				.setTabListener(new KimTabListener());
		Tab mTab2 = actionBar.newTab().setText("main_player")
				.setTabListener(new KimTabListener());
		Tab mTab3 = actionBar.newTab().setText("list")
				.setTabListener(new KimTabListener());

		actionBar.addTab(mTab1);
		actionBar.addTab(mTab2);
		actionBar.addTab(mTab3);
		// Step 4: default view
		// callLibraryListFragment();
	}

	private class KimTabListener implements ActionBar.TabListener {

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction arg1) {
			if (tab.getText().equals("main_player")) {
				// Step 2
				callMainPlayerFragment(true);
				// khong can dang ky listener khi choi xong
				// ben trong fragment no tu dang ky roi
				//actionBar.selectTab(actionBar.getTabAt(1));
			} else if (tab.getText().equals("library")) {
				callLibraryListFragment(true);
				//actionBar.selectTab(actionBar.getTabAt(0));
				// vi khi thoat fragment main player thi mat listener
				// begin: register listener for update next song auto
				PL.getMediaPlayer()
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								if (PL.requestNextSong()) {
									PL.Play();
								}
							}
						});
				// end

			} else if (tab.getText().equals("list")) {
				callLibraryEnqueueFragment(true, LI._enqueue);
				//actionBar.selectTab(actionBar.getTabAt(2));
				// vi khi thoat fragment main player thi mat listener
				// begin: register listener for update next song auto
				PL.getMediaPlayer()
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								if (PL.requestNextSong()) {
									PL.Play();
								}
							}
						});
				// end
			}
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (tab.getText().equals("main_player")) {
				// Step 2
				callMainPlayerFragment(true);
				// khong can dang ky listener khi choi xong
				// ben trong fragment no tu dang ky roi
				//actionBar.selectTab(actionBar.getTabAt(1));
			} else if (tab.getText().equals("library")) {
				callLibraryListFragment(true);
				//actionBar.selectTab(actionBar.getTabAt(0));
				// vi khi thoat fragment main player thi mat listener
				// begin: register listener for update next song auto
				PL.getMediaPlayer()
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								if (PL.requestNextSong()) {
									PL.Play();
								}
							}
						});
				// end

			} else if (tab.getText().equals("list")) {
				callLibraryEnqueueFragment(true, LI._enqueue);
				//actionBar.selectTab(actionBar.getTabAt(2));
				// vi khi thoat fragment main player thi mat listener
				// begin: register listener for update next song auto
				PL.getMediaPlayer()
						.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								if (PL.requestNextSong()) {
									PL.Play();
								}
							}
						});
				// end
			}
		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

		}

	}

	/*
	 * private void update_sample() { try { MySongDAO dao=new MySongDAO(this,
	 * null); dao.setSource(MySource.DB_SOURCE); MySong obj =
	 * dao.getAll().get(0); obj.getAlbum().setName("album3");
	 * obj.getArtist().setName("artist3"); obj.setTitle("title3"); obj.update();
	 * }catch(Exception e) {
	 * 
	 * } }
	 */
	private void callMainPlayerFragment(Boolean addToBackStack) {
		MainPlayerFragment fm = new MainPlayerFragment() {
		};
		FragmentTransaction frt = getFragmentManager().beginTransaction();
		frt.replace(layout_container, fm);
		//frt.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		if (addToBackStack) {
			frt.addToBackStack(MainPlayerFragment.class.getName());
		}
		frt.commit();
		// switch tab
		if(actionBar.getSelectedNavigationIndex()!=1)
		{
			actionBar.selectTab(actionBar.getTabAt(1));
		}
	}


	/**
	 * add to PL.songList
	 * 
	 * @param obj
	 */
	public void addToSongList(MySong obj) {
		if (!obj.isSongInList(LI._enqueue)) {
			LI._enqueue.add(obj);
		}
		// move enqueue to PL list
		PL.setSongsList(LI._enqueue);
	}
	/**
	 * =============================================================== Khu vuc
	 * cua Library
	 */
	private class Library {

		/**
		 * For MyLibaryDataProvider cached, Fragment use
		 */
		public ArrayList<MyAlbum> _albumsProvider = null;
		public ArrayList<MyArtist> _artistsProvider = null;
		public ArrayList<MyFolder> _foldersProvider = null;
		public ArrayList<MySong> _songsProvider = null;
		public ArrayList<MyPlayList> _playListsProvider = null;
		/**
		 * For All items cached
		 */
		public ArrayList<MyAlbum> _albumsAll = null;
		public ArrayList<MyArtist> _artistsAll = null;
		public ArrayList<MyFolder> _foldersAll = null;
		public ArrayList<MySong> _songsAll = null;
		public ArrayList<MyPlayList> _playListsAll = null;
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
	private void callLibraryAlbumsFragment(Boolean addToBackStack,
			ArrayList<MyAlbum> albums) {
		MyLibraryAlbumsFragment mFragment = new MyLibraryAlbumsFragment();
		LI._albumsProvider = albums;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(layout_container, mFragment);
		// since addToBackStacl allow user to navigate to blank screen
		// when app not swap fragment yet
		if (addToBackStack) {
			ft.addToBackStack(MyLibraryAlbumsFragment.class.getName());// very
																		// importance
		}
		ft.commit();
	}

	private void callLibraryArtistsFragment(Boolean addToBackStack,
			ArrayList<MyArtist> artists) {
		MyLibraryArtistsFragment mFragment = new MyLibraryArtistsFragment();
		LI._artistsProvider = artists;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(layout_container, mFragment);
		// since addToBackStacl allow user to navigate to blank screen
		// when app not swap fragment yet
		if (addToBackStack) {
			ft.addToBackStack(MyLibraryArtistsFragment.class.getName());// very
																		// importance
		}
		ft.commit();
	}

	private void callLibraryFoldersFragment(Boolean addToBackStack,
			ArrayList<MyFolder> folders) {
		MyLibraryFoldersFragment mFragment = new MyLibraryFoldersFragment();
		LI._foldersProvider = folders;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(layout_container, mFragment);
		// since addToBackStacl allow user to navigate to blank screen
		// when app not swap fragment yet
		if (addToBackStack) {
			ft.addToBackStack(MyLibraryFoldersFragment.class.getName());// very
																		// importance
		}
		ft.commit();
	}

	private void callLibraryListFragment(Boolean addToBackStack) {
		MyLibraryListFragment mFragment = new MyLibraryListFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(layout_container, mFragment);
		// since addToBackStacl allow user to navigate to blank screen
		// when app not swap fragment yet
		if (addToBackStack) {
			ft.addToBackStack(MyLibraryListFragment.class.getName());// very
																		// importance
		}
		ft.commit();
		// switch tab
		if(actionBar.getSelectedNavigationIndex()!=0)
		{
			actionBar.selectTab(actionBar.getTabAt(0));
		}
	}

	/**
	 * 
	 * @param addToBackStack
	 * @param songs
	 *            Songs want to display in Fragment
	 */
	private void callLibrarySongsFragment(Boolean addToBackStack,
			ArrayList<MySong> songs) {
		MyLibrarySongsFragment mFragment = new MyLibrarySongsFragment();
		// khong nen dung bundle.serialize de pass data, khi saveinstance rat de
		// bi loi
		// set temp song first
		LI._songsProvider = songs;// very importance
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(layout_container, mFragment);
		// since addToBackStacl allow user to navigate to blank screen
		// when app not swap fragment yet
		if (addToBackStack) {
			ft.addToBackStack(MyLibrarySongsFragment.class.getName());// very
																		// importance
			// Log.w("qd",MyLibrarySongsFragment.class.getName());
		}
		ft.commit();

	}

	private void callLibraryPlayListsFragment(Boolean addToBackStack,
			ArrayList<MyPlayList> playLists) {
		MyLibraryPlayListsFragment mFragment = new MyLibraryPlayListsFragment();
		// khong nen dung bundle.serialize de pass data, khi saveinstance rat de
		// bi loi
		// set temp song first
		LI._playListsProvider = playLists;// very importance
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(layout_container, mFragment);
		// since addToBackStacl allow user to navigate to blank screen
		// when app not swap fragment yet
		if (addToBackStack) {
			ft.addToBackStack(MyLibraryPlayListsFragment.class.getName());// very
																			// importance
			// Log.w("qd",MyLibrarySongsFragment.class.getName());
		}
		ft.commit();
	}

	private void callLibraryEnqueueFragment(Boolean addToBackStack,
			ArrayList<MySong> playLists) {
		MyLibraryEnqueueFragment mFragment = new MyLibraryEnqueueFragment();
		// khong nen dung bundle.serialize de pass data, khi saveinstance rat de
		// bi loi
		// set temp song first
		LI._enqueue = playLists;// very importance
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(layout_container, mFragment);
		// since addToBackStacl allow user to navigate to blank screen
		// when app not swap fragment yet
		if (addToBackStack) {
			ft.addToBackStack(MyLibraryEnqueueFragment.class.getName());// very
																		// importance
			// Log.w("qd",MyLibrarySongsFragment.class.getName());
		}
		ft.commit();
		// switch tab
		if(actionBar.getSelectedNavigationIndex()!=2)
		{
			actionBar.selectTab(actionBar.getTabAt(2));
		}
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
		// Log.w("qd",this.getClass().getName());
		if (data == null) {
			return;
		}
		Boolean dbChanged = data.getBooleanExtra(
				FolderChooserPreference.FOLDER_CHANGED_KEY, false);
		Boolean langChanged = data.getBooleanExtra(SettingsActivity.LANG_CHANGED_KEY, false);
		if(langChanged)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

		    builder.setTitle(getResources().getString(R.string.activity_exit_title));
		    builder.setMessage(getResources().getString(R.string.activity_restart_message));

		    builder.setPositiveButton(getResources().getString(R.string.activity_exit_positive), new DialogInterface.OnClickListener() {

		        public void onClick(DialogInterface dialog, int which) {
		            // Do nothing but close the dialog
		            dialog.dismiss();
		            //restart();
		        }

		    });

		    builder.setNegativeButton(getResources().getString(R.string.activity_exit_negative), new DialogInterface.OnClickListener() {

		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            // Do nothing
		            dialog.dismiss();
		        }
		    });

		    
		    AlertDialog alert = builder.create();
		    alert.show();
		    return;
		}
		//qd continue to work here
		//...
		if (dbChanged) {
			// backto root GUI first
			callLibraryListFragment(false);
			// switch tab
			actionBar.selectTab(actionBar.getTabAt(0));
			// reset all cached member related to DB
			resetLibraryDBState();
			
			//STOP music
			PL.Stop();
			//reset player
			PL.setNew(null, null);
			Toast.makeText(this, "DB Changed!", 1000).show();
		}
		return;
	}
	
	private void exit() {
		//move to library fragment first
        callLibraryListFragment(true);
        //release Media Player
        PL.release();
        //exit activity
        finish();
	}
	private void restart() {
		//move to library fragment first
        callLibraryListFragment(true);
        //release Media Player
        PL.release();
        //exit activity
        finish();
	}

	protected void InitMyLibrary() {
		// init DB DAOs
		resetLibraryDBState();
		// init memeber pre-values
		LI._enqueue = new ArrayList<MySong>();
		// View by default
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
		// Display songs of album
		callLibrarySongsFragment(true, current.getSongs());
	}

	@Override
	public void onLibraryArtistItemClick(MyArtist current,
			ArrayList<MyArtist> artists) {
		// Display songs of artist
		callLibrarySongsFragment(true, current.getSongs());
	}

	@Override
	public void onLibraryFolderItemClick(MyFolder current,
			ArrayList<MyFolder> folders) {
		// Display all recursive songs of folder
		callLibrarySongsFragment(true, current.getAllRecursiveSongs());
	}

	@Override
	public void onLibraryPlayListItemClick(MyPlayList current,
			ArrayList<MyPlayList> playlists) {
		// Display all recursive songs of playList
		callLibrarySongsFragment(true, current.getSongs());
	}

	@Override
	public void onLibraryItemClick(String itemId) {
		// see R.array.library_item_id_array for values of itemId can be
		if (itemId.toUpperCase().equals("ALLSONGS_ITEM")) {
			// cached because of time to load allrecursive song for count
			if (LI._songsAll == null) {
				LI._songsAll = LI._gDAOs.getMySongDAO().getAll();
			}
			callLibrarySongsFragment(true, LI._songsAll);
			return;
		} else if (itemId.toUpperCase().equals("ALBUMS_ITEM")) {
			// cached because of time to load allrecursive song for count
			if (LI._albumsAll == null) {
				LI._albumsAll = LI._gDAOs.getMyAlbumDAO().getAll();
			}
			callLibraryAlbumsFragment(true, LI._albumsAll);
			return;
		} else if (itemId.toUpperCase().equals("ARTISTS_ITEM")) {
			// cached because of time to load allrecursive song for count
			if (LI._artistsAll == null) {
				LI._artistsAll = LI._gDAOs.getMyArtistDAO().getAll();
			}
			callLibraryArtistsFragment(true, LI._artistsAll);// do the bi dung
																// do voi
																// MySQLiteHelper.close()
			return;
		} else if (itemId.toUpperCase().equals("FOLDERS_ITEM")) {
			// cached because of time to load allrecursive song for count
			if (LI._foldersAll == null) {
				LI._foldersAll = LI._gDAOs.getMyFolderDAO().getAll();
			}
			callLibraryFoldersFragment(true, LI._foldersAll);// be careful
																// because of
																// cache
			return;
		} else if (itemId.toUpperCase().equals("PLAYLISTS_ITEM")) {
			// cached because of time to load allrecursive song for count
			if (LI._playListsAll == null) {
				LI._playListsAll = LI._gDAOs.getMyPlayListDAO().getAll();
			}
			callLibraryPlayListsFragment(true, LI._playListsAll);// be careful
																	// because
																	// of cache
			return;
		} else if (itemId.toUpperCase().equals("ENQUEUE_ITEM")) {
			// cached because of time to load allrecursive song for count
			if (LI._enqueue == null) {
				LI._enqueue = new ArrayList<MySong>();
			}
			callLibraryEnqueueFragment(true, LI._enqueue);// be careful because
															// of cache
			return;
		}
		// default
		else {
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
		// Toast.makeText(this,
		// current.getPath().getAbsPath()+":"+playlist.size(), 200).show();
		// set enqueue
		LI._enqueue = playlist;
		// set player list from enqueue
		PL.setNew(current, playlist);
		PL.Play();
		// call fragment main player
		callMainPlayerFragment(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.action_settings)
		{
			//switch to Library first: to prevent BUG
			callLibraryListFragment(true);
			
			Intent setting = new Intent(this, SettingsActivity.class);
			startActivityForResult(setting, 1);
		}
		else if(item.getItemId()==R.id.action_exit)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

		    builder.setTitle(getResources().getString(R.string.activity_exit_title));
		    builder.setMessage(getResources().getString(R.string.activity_exit_message));

		    builder.setPositiveButton(getResources().getString(R.string.activity_exit_positive), new DialogInterface.OnClickListener() {

		        public void onClick(DialogInterface dialog, int which) {
		            // Do nothing but close the dialog
		            dialog.dismiss();
		            exit();
		        }

		    });

		    builder.setNegativeButton(getResources().getString(R.string.activity_exit_negative), new DialogInterface.OnClickListener() {

		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            // Do nothing
		            dialog.dismiss();
		        }
		    });

		    
		    AlertDialog alert = builder.create();
		    alert.show();
		}
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// do not call super or error may occur
		// when be in MyLibrarySongsFragment and minimize or switch to other
		// activity
		// may be serialize when passing bundle to fragment from this activity
		// fixed by use another approach
		super.onSaveInstanceState(outState);
	}
	private void resetLibraryDBCache()
	{
		//provider
		if(LI._foldersProvider!=null)
		{
			for(MyFolder item:LI._foldersProvider)
			{
				item.reset();
			}
		}
		if(LI._albumsProvider!=null)
		{
			for(MyAlbum item:LI._albumsProvider)
			{
				item.reset();
			}
		}
		if(LI._artistsProvider!=null)
		{
			for(MyArtist item:LI._artistsProvider)
			{
				item.reset();
			}
		}
		if(LI._songsProvider!=null)
		{
			for(MySong item:LI._songsProvider)
			{
				item.reset();
			}
		}
		if(LI._playListsProvider!=null)
		{
			for(MyPlayList item:LI._playListsProvider)
			{
				item.reset();
			}
		}
		//All
		if(LI._foldersAll!=null)
		{
			for(MyFolder item:LI._foldersAll)
			{
				item.reset();
			}
		}
		if(LI._albumsAll!=null)
		{
			for(MyAlbum item:LI._albumsAll)
			{
				item.reset();
			}
		}
		if(LI._artistsAll!=null)
		{
			for(MyArtist item:LI._artistsAll)
			{
				item.reset();
			}
		}
		if(LI._songsAll!=null)
		{
			for(MySong item:LI._songsAll)
			{
				item.reset();
			}
		}
		if(LI._playListsAll!=null)
		{
			for(MyPlayList item:LI._playListsAll)
			{
				item.reset();
			}
		}
		//enqueue
		if(LI._enqueue!=null)
		{
			for(MySong item:LI._enqueue)
			{
				item.reset();
			}
		}
		//playlist
		if(PL.getSongsList()!=null)
		{
			for(MySong item:PL.getSongsList())
			{
				item.reset();
			}
		}
	}
	/**
	 * Xoa tat ca cahed, release DB, va sau do tao moi lai
	 */
	private void resetLibraryDBState() {
		//Provider
		LI._albumsProvider=null;
		LI._artistsProvider=null;
		LI._foldersProvider=null;
		LI._playListsProvider=null;
		LI._songsProvider=null;
		//All
		LI._albumsAll=null;
		LI._artistsAll=null;
		LI._foldersAll=null;
		LI._playListsAll=null;
		LI._songsAll=null;
		
		LI._enqueue = new ArrayList<MySong>();//importance
		
		if (LI._gDAOs != null) {
			LI._gDAOs.release();
			LI._gDAOs = null;
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
		// Toast.makeText(this, "onLibrarySongItemLongClick", 300).show();
		songs_ctx_dialog = new MyLibrarySongsCtxDialog(this, current,
				new MyLibrarySongsCtxDialog.MyLibrarySongsCtxItemListener() {

					@Override
					public void OnLibrarySongsCtxClick_EDIT_TAG(MySong obj) {
						Log.w("qd", "edit tag clicked" + getClass().getName());
						songs_ctx_dialog.dismiss();
						// addToSongList(obj);
						// Bundle d=new Bundle();
						EditTagDialog a = new EditTagDialog(
								getApplicationContext(), obj,
								new EditDialogListener() {

									@Override
									public void onFinishEdit(MySong input) {
										Toast.makeText(getApplicationContext(), "Done", 1000).show();
										//reset DB cache
										resetLibraryDBCache();
									}
								});
						// editTag_ctx_dialog = new TestCtxDialog(_getContext(),
						// obj, null);
						// songs_ctx_dialog.dismiss();
						a.show(getFragmentManager(), "edittag");

						// editTag_ctx_dialog.show();
					}

					@Override
					public void OnLibrarySongsCtxClick_ADD_TO_PLAYLIST(
							MySong obj) {
						Log.w("qd", "add to playlist clicked"
								+ getClass().getName());
						songs_ctx_dialog.dismiss();
					}

					@Override
					public void OnLibrarySongsCtxClick_ADD_TO_ENQUEUE(MySong obj) {
						Log.w("qd", "add to enqueue clicked"
								+ getClass().getName());
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

	@Override
	public void onFinishEdit(MySong input) {
		
	}
	private void loadLanguage()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String lang = prefs.getString(SettingsActivity.LANG_KEY, "en_US");//coi file setting.xml de biet key
		Locale locale = new Locale(lang); 
		Locale.setDefault(locale);
		Configuration config = getResources().getConfiguration();
		config.locale = locale;
		getResources().updateConfiguration(config, null);
	}

	@Override
	public MyPlayer getDataProvider() {
		return PL;
	}
	
	private MyLibraryFoldersCtxDialog folders_ctx_dialog=null;
	@Override
	public void onLibraryFolderItemLongClick(MyFolder current,
			ArrayList<MyFolder> folders) {
		folders_ctx_dialog = new MyLibraryFoldersCtxDialog(this, current,
				new MyLibraryFoldersCtxDialog.MyLibraryFoldersCtxItemListener() {

					@Override
					public void OnLibraryFoldersCtxClick_ADD_TO_ENQUEUE(MyFolder obj) {
						for(MySong item:obj.getAllRecursiveSongs())
						{
							addToSongList(item);
						}
						folders_ctx_dialog.dismiss();
					}
				});
		folders_ctx_dialog.setTitle("Context dialog");
		folders_ctx_dialog.show();
	}
}