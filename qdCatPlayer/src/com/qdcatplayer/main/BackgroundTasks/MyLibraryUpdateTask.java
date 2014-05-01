package com.qdcatplayer.main.BackgroundTasks;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.FileSystem.MyFileChangesInterface;
import com.qdcatplayer.main.FileSystem.MyFolderChanges;

public class MyLibraryUpdateTask {
	private ArrayList<MyFolder> folders = null;
	private Context ctx = null;
	public interface OnBGTaskWorkingListener{
		public void onFinish();
		public void onProgressUpdating(Integer total, Integer current);
	}
	
	private OnBGTaskWorkingListener mListener=null;
	public MyLibraryUpdateTask(ArrayList<MyFolder> folders, Context ctx, OnBGTaskWorkingListener listener) {
		this.folders = folders;
		this.ctx=ctx;
		this.mListener = listener;
	}
	public Boolean start()
	{
		MyTask tsk = new MyTask();
		tsk.execute("");
		return true;
	}
	public class MyTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			//reset entire DB
			MyDBManager mn=new MyDBManager();
			mn.getHelper(ctx).resetDB();
			mn.getHelper().close();
			Log.w("qd", "Reset entire DB");
			
			MyFolderDAO dao = new MyFolderDAO(ctx, null);
			dao.setSource(MySource.DISK_SOURCE);
			//add new one based on folder choosed
			Integer total=0;
			Integer current=0;
			for(MyFolder item:folders)
			{
				item.setDao(dao);
				total+=item.getAllRecursiveSongs().size();
			}
			for(MyFolder item:folders)
			{
				//item.setDao(dao);
				ArrayList<MySong> songs = item.getAllRecursiveSongs();
				for(MySong song:songs)
				{
					song.insert();
					current++;
					publishProgress(total,current);
					Log.w("qd", "Inserted song:"+song.getPath().getAbsPath());
				}
			}
			//update tracking hash, switch to DB_SOURCE first
			dao.setSource(MySource.DB_SOURCE);
			/*
			for(MyFolder item:folders)
			{
				MyFolder new_obj = dao.getByAbsPath(item.getAbsPath());
				if(new_obj!=null)
				{
					
				}
			}
			*/
			dao.release();
			return "";
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mListener.onFinish();
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			mListener.onProgressUpdating(values[0], values[1]);
		}
	}
}
