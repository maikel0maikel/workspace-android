/**
 * Copyright CMW Mobile.com, 2010. 
 */
package com.qdcatplayer.main.Setting;

import java.util.ArrayList;
import java.util.HashMap;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.Entities.MyFolder;

import android.app.AlertDialog.Builder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import android.content.res.TypedArray;

import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;

import android.util.AttributeSet;
import android.util.Log;

import android.widget.ListAdapter;

/**
 * The ImageListPreference class responsible for displaying an image for each
 * item within the list.
 * @author Casper Wakkers
 */
public class FolderChooserPreference extends ListPreference {
	public interface MyItemClickListener {
		public void onClick(String absPath,Boolean isChecked);
	}
	/**
	 * Luu ds cac folder duoc chon khi co su thay doi tu ng dung
	 * Sau do khi click OK thi se su dung de lay ds cac Folder duoc chon cuoi cung
	 */
	HashMap<String, Boolean> chooseResult =new HashMap<String, Boolean>();
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
				public void onClick(String absPath, Boolean isChecked) {
					if(isChecked)
					{
						chooseResult.put(absPath, true);
						Log.w("qd", "Put "+absPath);
					}
					else
					{
						chooseResult.remove(absPath);
						Log.w("qd", "Remove "+absPath);
					}
				}
			});
		
		// Order matters.
		
		builder.setAdapter(listAdapter, this);
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.w("qd", "OK clicked");
				//FINISH
		
				for(String tmp:chooseResult.keySet())
				{
					Log.w("qd", tmp);
				}
				//clear after success
				chooseResult.clear();
			}
		});
		//do not call super or OK button will never appear
		//super.onPrepareDialogBuilder(builder);
	}
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
	}
	
}
