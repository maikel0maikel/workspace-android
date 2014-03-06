package com.bffmedia.hour9app;

import java.util.Calendar;

import com.bffmedia.hour9app.DatePickerDialogFragment.OnDateEnteredListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity implements DatePickerDialogFragment.OnDateEnteredListener {
	   private final static int DIALOG_DATE_PICKER = 1;
	    private int setYear;
	    private int setMonth;
	    private int setDay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btn1=(Button)findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BasicDialogFragment basicedialog=new BasicDialogFragment();
				//basicedialog.getDialog().setTitle("First name");
				//basicedialog.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
				basicedialog.show(getFragmentManager(), "basic");
			}
		});
		Button btn2=(Button)findViewById(R.id.button2);
		
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			DatePickerDialogFragment a=new DatePickerDialogFragment();
			a.show(getFragmentManager(), "date picker");		        
			}
		});
		Button bt3=(Button)findViewById(R.id.Button01);
		bt3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar today = Calendar.getInstance();
		        setYear = today.get(Calendar.YEAR);
		        setMonth = today.get(Calendar.MONDAY);
		        setDay = today.get(Calendar.DAY_OF_MONTH);
		        showDialog(DIALOG_DATE_PICKER);
//		        DatePickerDialog a=new DatePickerDialog(MainActivity.this,0,new OnDateSetListener() {
//					
//					@Override
//					public void onDateSet(DatePicker view, int year, int monthOfYear,
//							int dayOfMonth) {
//						// TODO Auto-generated method stub
//						
//					}
//				},2012,11,31);
//		        a.show();
			}
		});
		
		Button btn4=(Button)findViewById(R.id.Button02);
		btn4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			BasicAlertDialogFragment a=new BasicAlertDialogFragment();
			a.show(getFragmentManager(), "alert");
			}
		});
		Button btn5=(Button)findViewById(R.id.Button03);
		btn5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			ListDialogFragment a=new ListDialogFragment();
			a.show(getFragmentManager(), "list alert");
			}
		});
	}
	  @Override
	    protected Dialog onCreateDialog(int id){
	        switch(id){
	            case DIALOG_DATE_PICKER:
	                return new DatePickerDialog(this, mDateSetListener, setYear, setMonth, setDay);
	            }
	        return null;
	    }
	  	
	    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener(){
	        @Override
	        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
	            setYear = year;
	            setMonth = month;
	            setDay = day;
	            Toast.makeText(getApplicationContext(), day+"/"+month+"/"+year, Toast.LENGTH_SHORT).show();
	        }
	    };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void OnDateEntered(int year, int monthofYear, int dayofMonth) {
		// TODO Auto-generated method stub
		  Toast.makeText(getApplicationContext(), dayofMonth+"/"+monthofYear+"/"+year, Toast.LENGTH_SHORT).show();
	}

}
