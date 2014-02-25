package com.example.hour10app;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QdListViewFr extends ListFragment  {
	String data[];
	QdListViewItemClickListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.list_view_fr, container, false);
		//get Bundle
		Bundle bun=getArguments();
		data = bun.getStringArray("data");
		setListAdapter(new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1, data));
		
		return v;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mListener = (QdListViewItemClickListener)activity;
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		if(position==0)
		{
			mListener.OnListViewItemClick(R.layout.list_view_fr);
		} else if(position==1)
		{
			mListener.OnListViewItemClick(R.layout.grid_view_fr);
		}
		
	}
}
