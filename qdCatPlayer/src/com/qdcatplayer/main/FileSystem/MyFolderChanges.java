package com.qdcatplayer.main.FileSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.qdcatplayer.main.Libraries.MyMD5;

public class MyFolderChanges {
	private String path = null;
	private Long folderCount = 0l;
	private Long fileCount = 0l;
	private Long folderSize = 0l;
	ByteArrayOutputStream fileNameChain = null;
	private String md5 = null;
	private MyFileChangesInterface trigger = null;
	public MyFolderChanges(String path, MyFileChangesInterface trigger) {
		this.path = path;
		this.trigger = trigger;
	}
	/**
	 * Start load file system changes
	 * you must call getMD5 by yourself to got result
	 */
	public void start()
	{
		if(path!=null)
		{
			fileNameChain = new ByteArrayOutputStream();
			fileAsynctask doing= new fileAsynctask();
			doing.execute(getPath());
		}
	}
	private void onFinish()
	{
		//activate trigger
		trigger.onFinish(this);
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Long getFolderCount() {
		return folderCount;
	}
	public void setFolderCount(Long folderCount) {
		this.folderCount = folderCount;
	}
	public Long getFileCount() {
		return fileCount;
	}
	public void setFileCount(Long fileCount) {
		this.fileCount = fileCount;
	}
	public Long getFolderSize() {
		return folderSize;
	}
	public void setFolderSize(Long folderSize) {
		this.folderSize = folderSize;
	}
	public String getMd5() {
		//concat some info
		try {
			fileNameChain.write(
					(folderCount+
					fileCount+
					folderSize + "").getBytes()
					);
			md5 = MyMD5.md5(fileNameChain.toByteArray());
			return md5;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	//Asyntask for loading
	private class fileAsynctask extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPostExecute(String result) {
			onFinish();
		}
		@Override
		protected String doInBackground(String... params) {
			Looper(params[0]);
			return "";
		}
		private void Looper(String root)
		{
			File f = new File(root);
			//Have to separate load all direct file first
			for(File tmp:f.listFiles())
			{
				if(tmp.isFile())
				{
					fileCount++;
					folderSize+=tmp.length();
					try {
						fileNameChain.write(tmp.getName().getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//then loop for folder
			for(File tmp:f.listFiles())
			{
				if(tmp.isDirectory())
				{
					folderCount++;
					Looper(tmp.getAbsolutePath());
				}
			}
		}
		
	}
}
