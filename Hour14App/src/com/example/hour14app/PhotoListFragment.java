package com.example.hour14app;
import java.util.ArrayList;

import com.example.hour14app.MainActivity;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class PhotoListFragment extends ListFragment    {
	Cursor mPhotoCursor;
	SimpleCursorAdapter mAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//get favarite phôt from URI
		mPhotoCursor = getActivity().managedQuery(
				Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI,"favourite"),
						null, null, null, null);
		Log.w("qd","PhotoListFragment call 'get favourite' imgs via URI InstagramContentProvider");
		mAdapter = new SimpleCursorAdapter(getActivity(), 
				android.R.layout.simple_list_item_1, 
				mPhotoCursor, //Cursor
				new String[] {"title"},
				new int[] { android.R.id.text1 }, 0); 
		setListAdapter(mAdapter);
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mPhotoCursor.moveToPosition(position);
		InstagramPhoto tmp=InstagramPhotoDbAdapter.getPhotoFromCursor(mPhotoCursor);
		String img_url = tmp.img_thumb_url;
		Toast.makeText(this.getActivity().getApplicationContext(), img_url, Toast.LENGTH_SHORT).show();
		((MainActivity)this.getActivity()).Set_Image(img_url);
		Log.w("qd","PhotoListFragment wants show img on 'main layout'");
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
} 
