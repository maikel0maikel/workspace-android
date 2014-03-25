/**
 * Copyright CMW Mobile.com, 2010. 
 */
package com.qdcatplayer.main.Setting2;

import java.util.ArrayList;
import java.util.HashMap;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.entities.MyFolder;

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
		public void onClick(String absPath);
	}
	HashMap<MyFolder, Boolean> map =new HashMap<MyFolder, Boolean>();
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
		MyFolder fd = new MyFolder("/sdcard/music");
		fd.setDao(dao);
		
		ArrayList<MyFolder> folders = fd.getChildFolders();
		
		ListAdapter listAdapter = new FolderArrayAdapter(getContext(),
			R.layout.setting_folder_chooser, folders, null, fd, new MyItemClickListener() {
				
				@Override
				public void onClick(String absPath) {
					Log.w("qd", absPath);
				}
			});
		
		// Order matters.
		
		builder.setAdapter(listAdapter, this);
		super.onPrepareDialogBuilder(builder);
	}
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
	}
}
