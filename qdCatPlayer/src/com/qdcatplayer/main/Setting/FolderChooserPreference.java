/**
 * Copyright CMW Mobile.com, 2010. 
 */
package com.qdcatplayer.main.Setting;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.BackgroundTasks.MyLibraryUpdateTask;
import com.qdcatplayer.main.BackgroundTasks.MyLibraryUpdateTask.OnBGTaskWorkingListener;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Libraries.MyNumberHelper;

/**
 * The ImageListPreference class responsible for displaying an image for each
 * item within the list.
 * @author Casper Wakkers
 */
public class FolderChooserPreference extends ListPreference{
	
	public interface OnFolderChooserFinishListener {
		public void OnFolderChooserFinish();
	}
	public static final String ONFINISH_LISTENER_KEY = "onfinish_listener";
	public static final String FOLDER_CHANGED_KEY = "folder_changed";
	public interface MyItemClickListener {
		public void onClick(MyFolder fd,Boolean isChecked);
	}
	/**
	 * Luu ds cac folder duoc chon khi co su thay doi tu ng dung
	 * Sau do khi click OK thi se su dung de lay ds cac Folder duoc chon cuoi cung
	 */
	HashMap<String, MyFolder> chooseResult =new HashMap<String, MyFolder>();
	public FolderChooserPreference(Context arg0) {
		super(arg0);
	}
	/**
	 * Constructor of the ImageListPreference. Initializes the custom images.
	 * @param context application context.
	 * @param attrs custom xml attributes.
	 */
	
	/**
	 * Rat quan trong, neu khong co contructor nay thi o
	 * PrefenceScreen khong the goi khoi tao nhu mot element duoc
	 * @param context
	 * @param attrs
	 */
	public FolderChooserPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	private Boolean pushToChooser(MyFolder obj)
	{
		chooseResult.put(obj.getAbsPath(), obj);
		return true;
	}
	private Boolean removeFromChooser(MyFolder obj)
	{
		chooseResult.remove(obj.getAbsPath());
		return true;
	}
	private HashMap<String, MyFolder> validateChooser(HashMap<String, MyFolder> input)
	{
		//kiem tra coi folder co parent nam trong list hay chua
		Boolean needed = null;
		HashMap<String, MyFolder> finalChooserResult=new HashMap<String, MyFolder>();
		for(MyFolder item:input.values())
		{
			needed = true;
			for(MyFolder item2:input.values())
			{
				if(item.getAbsPath().equals(item2.getAbsPath()))
				{
					continue;
				}
				if(item.isSubFolderOf(item2)){
					needed=false;
					break;
				}
			}
			if(needed)
			{
				finalChooserResult.put(item.getAbsPath(), item);
			}
		}
		return finalChooserResult;
	}
	/**
	 * khi click moi bat dau chay
	 */
	protected void onPrepareDialogBuilder(Builder builder) {
		
		MyFolderDAO dao = new MyFolderDAO(getContext(), null);
		dao.setSource(MySource.DISK_SOURCE);
		MyFolder fd = new MyFolder("/mnt");
		fd.setDao(dao);
		
		ArrayList<MyFolder> folders = fd.getChildFolders();
		
		ListAdapter listAdapter = new FolderArrayAdapter(getContext(),
			R.layout.setting_folder_chooser_item, folders, null, fd, new MyItemClickListener() {
				
				@Override
				public void onClick(MyFolder fd, Boolean isChecked) {
					if(isChecked)
					{
						//chooseResult.put(fd.getAbsPath(), fd);
						pushToChooser(fd);
						Log.w("qd", "Put "+fd.getAbsPath());
					}
					else
					{
						//chooseResult.remove(fd.getAbsPath());
						removeFromChooser(fd);
						Log.w("qd", "Remove "+fd.getAbsPath());
					}
				}
			});
		
		// Order matters.
		
		builder.setAdapter(listAdapter, this);
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.w("qd", "OK clicked");
				Log.w("qd","Chooder folder list before validate:");
				for(MyFolder item:chooseResult.values())
				{
					Log.w("qd", item.getAbsPath());
				}
				//validate first
				chooseResult= validateChooser(chooseResult);
				Log.w("qd","Final folder list to fetch after validate:");
				for(MyFolder item:chooseResult.values())
				{
					Log.w("qd", item.getAbsPath());
				}
				
				if(chooseResult.size()<=0)
				{
					Toast.makeText(getContext(), "Nothing changed", 300).show();
					return;
				}
				//FINISH
				ArrayList<MyFolder> list_tmp = new ArrayList<MyFolder>();
				list_tmp.addAll(chooseResult.values());
				MyLibraryUpdateTask tsk=new MyLibraryUpdateTask(
						list_tmp,
						getContext(),
						new OnBGTaskWorkingListener() {
							
							@Override
							public void onFinish() {
								//clear after success
								chooseResult.clear();
								//stop progress bar...
								/*
								SharedPreferences pref = getPreferenceManager().getSharedPreferences();
								Editor edit = pref.edit();
								edit.remove("key").commit();//van con bi double event
								*/
								Log.w("qd","Folder changed");
								getSharedPreferences().edit().putBoolean(FOLDER_CHANGED_KEY, true).commit();
								//unlock asynctask wait
								//aSyncTaskFinish=true;
								setSummary(summary_bk);
							}

							@Override
							public void onProgressUpdating(Integer total,
									Integer current) {
								Log.w("qd",current+"/"+total);
								setSummary("Progress: "+current+"/"+total+" ("+(MyNumberHelper.round((double)current/total*100))+"%)");
							}
						});
				tsk.start();
				
				summary_bk = String.valueOf(getSummary());
				setSummary("Waiting...");
			}
		});
		//do not call super or OK button will never appear
		//super.onPrepareDialogBuilder(builder);
		
	}
	private String summary_bk = "";
	private Boolean aSyncTaskFinish=false;
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
	}
	@Override
	protected void onAttachedToActivity() {
		// TODO Auto-generated method stub
		super.onAttachedToActivity();
	}
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
	}
}
