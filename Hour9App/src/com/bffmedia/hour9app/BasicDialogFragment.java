package com.bffmedia.hour9app;

import com.bffmedia.hour9app.MainActivity;
import com.bffmedia.hour9app.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BasicDialogFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState)
	{
		View v=inflater.inflate(R.layout.basic_dialog_fragment,container,false);
		getDialog().setTitle("First name");
		Button btn=(Button)v.findViewById(R.id.buttonCancel);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BasicDialogFragment.this.dismiss();
			}
		});
		return v;
	}

}
