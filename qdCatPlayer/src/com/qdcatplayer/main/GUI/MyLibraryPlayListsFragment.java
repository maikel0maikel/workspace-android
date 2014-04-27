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
import com.qdcatplayer.main.Entities.MyPlayList;

/**
 * Set albums (ArrayList<MyAlbum>) over bundle by
 * using bundle.setSerializable(MyLibraryAlbumsFragment.SONGS, albums)
 * @author admin
 *
 */
public class MyLibraryPlayListsFragment extends ListFragment {
	public static String PLAYLISTS = "PLAYLISTS";
	public interface MyLibraryPlayListItemClickListener {
		public void onLibraryPlayListItemClick(MyPlayList current, ArrayList<MyPlayList> playlists);
	}

	private class MyLibraryPlayListsAdapter extends ArrayAdapter<MyPlayList> {
		private class ViewHolder {
			private MyPlayList playlist = null;
			private TextView tv_name = null;
			private TextView tv_totalSongs = null;

			public ViewHolder(MyPlayList playlist, TextView tv_name,
					TextView tv_totalSongs) {
				this.tv_name = tv_name;
				this.tv_totalSongs = tv_totalSongs;
				this.playlist = playlist;
				
			}

			public MyPlayList getPlayList() {
				return playlist;
			}

			public void setPlayList(MyPlayList playlist) {
				this.playlist = playlist;
			}

			public TextView getTv_name() {
				return tv_name;
			}

			public void setTv_name(TextView tv_name) {
				this.tv_name = tv_name;
			}

			public TextView getTv_totalSongs() {
				return tv_totalSongs;
			}

			public void setTv_totalSongs(TextView tv_totalSongs) {
				this.tv_totalSongs = tv_totalSongs;
			}


		}

		private MyLibraryPlayListItemClickListener mListener = null;
		private ArrayList<MyPlayList> playlists = null;

		public MyLibraryPlayListsAdapter(Context context, int textViewResourceId,
				ArrayList<MyPlayList> objects, MyLibraryPlayListItemClickListener listener) {

			super(context, textViewResourceId, objects);
			playlists = objects;
			mListener = listener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv_name;
			TextView tv_playlist;
			TextView tv_totalSongs;
			ViewHolder holder;
			MyPlayList playlist = playlists.get(position);
			if (convertView == null) {
				/*LayoutInflater inflater = ((Activity) getContext())
						.getLayoutInflater();*/
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.library_playlists_listview_item,
						parent, false);
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolder holder = (ViewHolder) v.getTag();
						MyPlayList playlist = holder.getPlayList();
						mListener.onLibraryPlayListItemClick(playlist, playlists);
					}
				});
				tv_name = (TextView) convertView
						.findViewById(R.id.playList_name);
				tv_totalSongs = (TextView) convertView
						.findViewById(R.id.playList_totalSongs);
				holder = new ViewHolder(playlist, tv_name, tv_totalSongs);
			} else {
				holder = (ViewHolder) convertView.getTag();
				tv_name = holder.getTv_name();
				tv_totalSongs = holder.getTv_totalSongs();
				holder.setPlayList(playlist);
			}
			tv_name.setText(playlist.getName());
			tv_totalSongs.setText(playlist.getSongs().size()+" [songs]");
			convertView.setTag(holder);
			return convertView;
		}
	}

	private MyLibraryPlayListItemClickListener mListener = null;
	public ArrayList<MyPlayList> playlists = null;

	public MyLibraryPlayListsFragment(){	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		playlists = ((_MyLibaryDataProvider)getActivity()).getPlayLists();
		if(playlists==null)
		{
			playlists = new ArrayList<MyPlayList>();//by default
		}
		
		MyLibraryPlayListsAdapter adp = new MyLibraryPlayListsAdapter(getActivity().getApplicationContext(),
				R.layout.library_playlists_listview_item, playlists, new MyLibraryPlayListItemClickListener() {

					@Override
					public void onLibraryPlayListItemClick(MyPlayList current,
							ArrayList<MyPlayList> playlists_) {
						mListener.onLibraryPlayListItemClick(current, playlists_);
					}

					
					
				});
		
		setListAdapter(adp);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		mListener = (MyLibraryPlayListItemClickListener) activity;
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
}
