package com.bffmedia.hour9app;

import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.Dialog;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	interface OnDateEnteredListener{
		public void OnDateEntered(int year,int monthofYear, int dayofMonth);
	}
	OnDateEnteredListener mListener;
	Calendar now=Calendar.getInstance();
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState){
		DatePickerDialog dateDialog=new DatePickerDialog(this.getActivity(),
				this,
				now.get(Calendar.YEAR),
				now.get(Calendar.MONTH),
				now.get(Calendar.DAY_OF_MONTH));
		return dateDialog;
	}
	@Override
	public void onDateSet(DatePicker view,int year, int monthofYear,int dayofmonth){
		mListener.OnDateEntered(year, monthofYear+1, dayofmonth);
	}
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mListener=(OnDateEnteredListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString()+" must implement OnDateEnteredListener");
		}
	}

}
