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
import com.qdcatplayer.main.Entities.MySong;

public class MyLibrarySongsCtxDialog extends Dialog {
	private String[] values = null;
	private String[] ids = null;

	public interface MyLibrarySongsCtxItemListener {
		public void OnLibrarySongsCtxClick_ADD_TO_ENQUEUE(MySong obj);

		public void OnLibrarySongsCtxClick_ADD_TO_PLAYLIST(MySong obj);

		public void OnLibrarySongsCtxClick_EDIT_TAG(MySong obj);
	}

	private MyLibrarySongsCtxItemListener mListener = null;
	private MySong obj = null;

	public MyLibrarySongsCtxDialog(Context context, MySong obj,
			MyLibrarySongsCtxItemListener listener) {
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
					mListener.OnLibrarySongsCtxClick_ADD_TO_ENQUEUE(obj);
				} else if (ids[pos].equals("ADD_TO_PLAYLIST")) {
					mListener.OnLibrarySongsCtxClick_ADD_TO_PLAYLIST(obj);
				} else if (ids[pos].equals("EDIT_TAG")) {
					mListener.OnLibrarySongsCtxClick_EDIT_TAG(obj);
				}
			}
		});

	}

	private HashMap<String, String> map = null;

	private void loadResource() {
		ids = getContext().getResources().getStringArray(
				R.array.library_ctx_songs_id_array);
		values = getContext().getResources().getStringArray(
				R.array.library_ctx_songs_value_array);

		map = new HashMap<String, String>();
		for (int i = 0; i < ids.length; i++) {
			map.put(ids[i], values[i]);
		}
	}

}
