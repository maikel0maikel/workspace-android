package com.example.hour10app;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class QdSecondFr extends Fragment  {
	String data[];
	QdListViewItemClickListener mListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//get Bundle
		Bundle bun=getArguments();
		data = bun.getStringArray("data");
		int layout_int = bun.getInt("layout_int");
		View v=null;
		//
		switch(layout_int)
		{
			case R.layout.list_view_fr:
				v  = (ListView) inflater.inflate(layout_int, container, false);
				((ListView) v).setAdapter(new ArrayAdapter<String>(this.getActivity(),
						android.R.layout.simple_list_item_1, data));
				break;
			case R.layout.grid_view_fr:
				v  = (GridView) inflater.inflate(layout_int, container, false);
				((GridView) v).setAdapter(new ArrayAdapter<String>(this.getActivity(),
						android.R.layout.simple_list_item_1, data));
				break;
		}
		return v;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mListener = (QdListViewItemClickListener)activity;
	}
	
	
}
