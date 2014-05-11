package com.qdcatplayer.main.GUI.CtxDialog;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.IInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MySong;

public class TestCtxDialog extends Dialog {
	public interface MyLibrarySongsCtxItemListener {
		public void OnLibrarySongsCtxClick_ADD_TO_ENQUEUE(MySong obj);
		public void OnLibrarySongsCtxClick_ADD_TO_PLAYLIST(MySong obj);
		public void OnLibrarySongsCtxClick_EDIT_TAG(MySong obj);
	}
	private MyLibrarySongsCtxItemListener mListener=null;
	private MySong obj = null;
	public TestCtxDialog(Context context, MySong obj, MyLibrarySongsCtxItemListener listener) {
		super(context);
		this.mListener=listener;
		this.obj = obj;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittag_dialog_fragment);
	}
}
