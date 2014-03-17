package com.qdcatplayer.main;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.MyAlbumDAO;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MyFolder;
import com.qdcatplayer.main.entities.MyPath;
import com.qdcatplayer.main.entities.MySong;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
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
		
		/*
		MyAlbumDAO dao=new MyAlbumDAO(getApplicationContext(), null);
		MyAlbum ma=new MyAlbum("er234frdt");
		ma.setDao(dao);
		ma.insert();
		dao.release();
		dao=null;
		*/
		
		
		//MyFolder f1=new MyFolder("/sdcard/music");
		//f1.getAllRecursiveSongs();
		
		
		/*
		MySongDAO d=new MySongDAO(getApplicationContext(),null);
		
		d.release();
		
		
		Vd f=new Vd();
		f.execute("");
		Vd f1=new Vd();
		f1.execute("");
		Vd f2=new Vd();
		f2.execute("");
		*/
		/*
		ArrayList<MySong> s1= d1.getAll();
		for(MySong item:s1)
		{
			Log.w("qd", item.getTitle());
			d1.delete(item);
		}
		d1.release();
		*/
		
		MyFolder fd=new MyFolder("/sdcard/music");
		ArrayList<MySong> ss = fd.getAllRecursiveSongs();
		MySongDAO dao=new MySongDAO(getApplicationContext(),null);
		for(MySong item:ss)
		{
			item.setDao(dao);
			//item.loadAllProperties();
			item.insert();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
