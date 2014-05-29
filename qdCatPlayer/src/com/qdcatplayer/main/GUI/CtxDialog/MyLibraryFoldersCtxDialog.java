package com.qdcatplayer.main.GUI.CtxDialog;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MyFolder;

public class MyLibraryFoldersCtxDialog extends Dialog {
	private String[] values = null;
	private String[] ids = null;

	public interface MyLibraryFoldersCtxItemListener {
		public void OnLibraryFoldersCtxClick_ADD_TO_ENQUEUE(MyFolder obj);
	}

	private MyLibraryFoldersCtxItemListener mListener = null;
	private MyFolder obj = null;

	public MyLibraryFoldersCtxDialog(Context context, MyFolder obj,
			MyLibraryFoldersCtxItemListener listener) {
		super(context);
		this.mListener = listener;
		this.obj = obj;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_ctx_dialog_songs);
		ListView lv = (ListView) findViewById(R.id.library_ctx_songs_listView);
		loadResource();
		ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_list_item_1, values);
		lv.setAdapter(adp);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (ids[pos].equals("ADD_TO_ENQUEUE")) {
					mListener.OnLibraryFoldersCtxClick_ADD_TO_ENQUEUE(obj);
				}
			}
		});

	}

	private HashMap<String, String> map = null;

	private void loadResource() {
		ids = getContext().getResources().getStringArray(
				R.array.library_ctx_folders_id_array);
		values = getContext().getResources().getStringArray(
				R.array.library_ctx_folders_value_array);

		map = new HashMap<String, String>();
		for (int i = 0; i < ids.length; i++) {
			map.put(ids[i], values[i]);
		}
	}

}
