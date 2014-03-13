package com.qdcatplayer.main;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.objects.MyFolder;
import com.qdcatplayer.main.objects.MyPath;
import com.qdcatplayer.main.objects.MySong;

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
		MySongDAO d=new MySongDAO(getApplicationContext());
		MySong obj=d.getById(1);
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
		MySongDAO dao=new MySongDAO(getApplicationContext());
		for(MySong item:ss)
		{
			Log.w("qd",item.getPath().getFileName() + item.getPath().getFileExtension(true));
			//dao.insert(item);//not test yet
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
			MySongDAO d2=new MySongDAO(getApplicationContext());
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
