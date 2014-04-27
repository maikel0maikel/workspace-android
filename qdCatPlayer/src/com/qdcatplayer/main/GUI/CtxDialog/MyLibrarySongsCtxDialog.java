package com.qdcatplayer.main.GUI.CtxDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MySong;

public class MyLibrarySongsCtxDialog extends Dialog {
	public interface MyLibrarySongsCtxItemListener {
		public void OnLibrarySongsCtxClick_ADD_TO_ENQUEUE();
		public void OnLibrarySongsCtxClick_ADD_TO_PLAYLIST();
		public void OnLibrarySongsCtxClick_EDIT_TAG();
	}
	private MyLibrarySongsCtxItemListener mListener=null;
	private MySong obj = null;
	public MyLibrarySongsCtxDialog(Context context) {
		super(context);
	}
	public MyLibrarySongsCtxDialog(Context context, MySong obj, MyLibrarySongsCtxItemListener listener) {
		super(context);
		this.mListener=listener;
		this.obj = obj;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_ctx_dialog_songs);
	}

}
