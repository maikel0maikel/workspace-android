package com.qdcatplayer.main.GUI.CtxDialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MySong;

@SuppressLint("ValidFragment")
public class EditTagDialog extends DialogFragment {
	public interface EditDialogListener {
		void onFinishEdit(MySong input);
	}

	MySong obj = null;
	Context mContext;
	EditDialogListener mListener = null;

	public EditTagDialog(Context ctx, MySong song, EditDialogListener listener) {
		super();
		mContext = ctx;
		this.obj = song;
		this.mListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		// return super.onCreateView(inflater, container, saveInstanceState);
		View v = inflater.inflate(R.layout.edittag_dialog_fragment, container,
				true);
		getDialog().setTitle("Edit tag");
		Button btnOK = (Button) v.findViewById(R.id.buttonOK);
		Button btnCancel = (Button) v.findViewById(R.id.buttonCancel);
		final EditText txtTitle = (EditText) v.findViewById(R.id.txtTitle);
		final EditText txtArtist = (EditText) v.findViewById(R.id.txtArtist);
		final EditText txtAlbum = (EditText) v.findViewById(R.id.txtAlbum);
		txtTitle.setText(obj.getTitle());
		if (obj.getArtist() == null)
			txtArtist.setText("Unknown");
		else
			txtArtist.setText(obj.getArtist().getName());
		if (obj.getAlbum() == null)
			txtAlbum.setText("Unknown");
		else
			txtAlbum.setText(obj.getAlbum().getName());
		txtTitle.requestFocus();
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditTagDialog.this.dismiss();
			}
		});
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				obj.getAlbum().setName(txtAlbum.getText().toString());
				obj.getArtist().setName(txtArtist.getText().toString());
				obj.setTitle(txtTitle.getText().toString());
				obj.update();
				mListener.onFinishEdit(obj);
				EditTagDialog.this.dismiss();
			}
		});
		return v;
	}
}