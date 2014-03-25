package com.qdcatplayer.main;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import com.j256.ormlite.dao.ForeignCollection;
import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.MyAlbumDAO;
import com.qdcatplayer.main.DAOs.MyArtistDAO;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.FileSystem.MyFileChangesInterface;
import com.qdcatplayer.main.FileSystem.MyFolderChanges;
import com.qdcatplayer.main.Setting.SettingListActivity;
import com.qdcatplayer.main.Setting2.SettingsActivity;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MyArtist;
import com.qdcatplayer.main.entities.MyFolder;
import com.qdcatplayer.main.entities.MyPath;
import com.qdcatplayer.main.entities.MySong;
import com.qdcatplayer.main.libraries.MyMD5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileObserver;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		MyDBManager mn=new MyDBManager();
		MySQLiteHelper h=mn.getHelper(getApplicationContext());
		h.getWritableDatabase();
		
		LoadToDB();
		//what the fuck
		/*
		MyAlbumDAO dao = new MyAlbumDAO(getApplicationContext(), null);
		dao.setSource(MySource.DB_SOURCE);
		MyAlbum obj=new MyAlbum();
		obj.setId(2);
		obj.setDao(dao);
		obj.load();
		
		obj.getSongs();
		obj.getName();
		*/
		//SettingListActivity m = new SettingListActivity();
		
		
		Intent setting = new Intent(MainActivity.this, SettingsActivity.class);
		startActivity(setting);
		
	}
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		return true;
		
	}
	class Vd extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			MySongDAO d2=new MySongDAO(getApplicationContext(),null);
			ArrayList<MySong> s1= d2.getAll();
			for(MySong item:s1)
			{
				Log.w("qd_a", item.getTitle());
			}
			d2.release();
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
}
