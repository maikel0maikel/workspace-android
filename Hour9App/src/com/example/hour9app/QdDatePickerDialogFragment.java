package com.example.hour9app;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

public class QdDatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	public QdDateEnteredListener mListener;
	Calendar qdNow = Calendar.getInstance();
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateDialog(savedInstanceState);
		DatePickerDialog tmp=new DatePickerDialog(this.getActivity(), this, qdNow.get(Calendar.YEAR),
				qdNow.get(Calendar.MONTH), qdNow.get(Calendar.DAY_OF_MONTH));
		
		return tmp;
		
	}
	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		mListener.OnDateEntered(arg1, arg2, arg3);//y,m,d
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
		mListener=(QdDateEnteredListener)activity;
		}catch(Exception ex)
		{
			Log.w("qd",ex.toString());
		}
	}
}
