package com.qdcatplayer.main.GUI;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.GUI.MainPlayerFragment.MyMainPLayerDataProvider;
import com.qdcatplayer.main.SharedAdapter.MyLibrarySongsAdapter;

/**
 * Set songs (ArrayList<MySong>) over bundle by
 * using bundle.setSerializable(MyLibrarySongsFragment.SONGS, songs)
 * @author admin
 *
 */
public class MyLibrarySongsFragment extends ListFragment {
	public static String SONGS = "SONGS";
	public interface MyLibrarySongItemClickListener {
		public void onLibrarySongItemClick(MySong current, ArrayList<MySong> songs);
		public void onLibrarySongItemLongClick(MySong current, ArrayList<MySong> songs);
	}

	private MyLibrarySongItemClickListener mListener = null;
	private MyMainPLayerDataProvider dataProvider = null;
	private ArrayList<MySong> songs = null;

	public MyLibrarySongsFragment(){	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		songs = ((_MyLibaryDataProvider)getActivity()).getSongs();
		if(songs==null)
		{
			songs = new ArrayList<MySong>();//by default
		}
		
		MyLibrarySongsAdapter adp = new MyLibrarySongsAdapter(getActivity().getApplicationContext(),
				R.layout.library_songs_listview_item, songs, new MyLibrarySongItemClickListener() {
					@Override
					public void onLibrarySongItemClick(MySong current, ArrayList<MySong> playlist) {
						mListener.onLibrarySongItemClick(current, playlist);
					}

					@Override
					public void onLibrarySongItemLongClick(MySong current,
							ArrayList<MySong> songs) {
						mListener.onLibrarySongItemLongClick(current, songs);
					}
				}, dataProvider);
		setListAdapter(adp);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		mListener = (MyLibrarySongItemClickListener) activity;
		dataProvider = (MyMainPLayerDataProvider)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.library_listview, container, false);
		return v;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);//not fail here
	}
	
}
