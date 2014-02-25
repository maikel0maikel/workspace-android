package com.example.hour8app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentYesNo extends Fragment{
	OnAnswerSelectedListener mListener;//to hold Activity for callback when Yes/No button is clicked
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.w("qd", "qd FragmentYesNo onActivityCreated!");
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("qd", "qd FragmentYesNo onCreate!");
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.w("qd", "qd FragmentYesNo onCreateView!");
		
		View v = inflater.inflate(R.layout.qd_question_fragment, container, false);//gọi hiển thị layout được chỉ định
		//get question from bundle, must setArgument from Activity first
		Bundle data = getArguments();
		TextView tv=(TextView) v.findViewById(R.id.textView_question);
		tv.setText(data.getString("question"));
		
		Button buttonYes = (Button) v.findViewById(R.id.button_yes);
		buttonYes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.OnAnswerSelected("Yes");//callback to Activy via member varible
			}
		});
		
		Button buttonNo = (Button) v.findViewById(R.id.button_no);
		buttonNo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.OnAnswerSelected("No");//callback to Activy via member varible
			}
		});

		return v;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnAnswerSelectedListener) activity;//Cast "host activity" to member var for callback 
			Log.w("qd", "qd FragmentYesNo onAttach!");
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
			+ " must implement OnAnswerSelectedListener");
		}
	}
}
