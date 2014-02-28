package com.example.hour14app;
import java.util.ArrayList;

import com.example.hour14app.MainActivity;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class PhotoListFragment extends ListFragment    {
	String[] mTitles;
	String[] mUrl;
	MainActivity current_ac;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MainActivity currentActivity = (MainActivity) this.getActivity();
		ArrayList <InstagramPhoto> photos = currentActivity.getPhotos();
		mTitles = new String[photos.size()];
		mUrl = new String[photos.size()];
		for (int i=0; i < photos.size(); i++){
			mTitles[i] =photos.get(i).title;
			mUrl[i] =photos.get(i).img_thumb_url;
		}
		setListAdapter(new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1, mTitles));
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(this.getActivity().getApplicationContext(), mUrl[position], Toast.LENGTH_SHORT).show();
		((MainActivity)this.getActivity()).Set_Image(mUrl[position]);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
} 
