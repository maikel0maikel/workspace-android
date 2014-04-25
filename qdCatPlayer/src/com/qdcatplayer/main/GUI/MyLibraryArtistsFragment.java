package com.qdcatplayer.main.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MySong;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Set albums (ArrayList<MyAlbum>) over bundle by
 * using bundle.setSerializable(MyLibraryAlbumsFragment.SONGS, albums)
 * @author admin
 *
 */
public class MyLibraryArtistsFragment extends ListFragment {
	public static String ARTISTS = "ARTISTS";
	public interface MyLibraryArtistItemClickListener {
		public void onLibraryArtistItemClick(MyArtist current, ArrayList<MyArtist> artists);
	}

	private class MyLibraryArtistsAdapter extends ArrayAdapter<MyArtist> {
		private class ViewHolder {
			private MyArtist artist = null;
			private TextView tv_name = null;
			private TextView tv_totalSongs = null;

			public ViewHolder(MyArtist artist, TextView tv_name,
					TextView tv_totalSongs) {
				this.tv_name = tv_name;
				this.tv_totalSongs = tv_totalSongs;
				this.artist = artist;
				
			}

			public MyArtist getArtist() {
				return artist;
			}

			public void setArtist(MyArtist artist) {
				this.artist = artist;
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

		private MyLibraryArtistItemClickListener mListener = null;
		private ArrayList<MyArtist> artists = null;

		public MyLibraryArtistsAdapter(Context context, int textViewResourceId,
				ArrayList<MyArtist> objects, MyLibraryArtistItemClickListener listener) {

			super(context, textViewResourceId, objects);
			artists = objects;
			mListener = listener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv_name;
			TextView tv_artist;
			TextView tv_totalSongs;
			ViewHolder holder;
			MyArtist artist = artists.get(position);
			if (convertView == null) {
				/*LayoutInflater inflater = ((Activity) getContext())
						.getLayoutInflater();*/
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.library_artists_listview_item,
						parent, false);
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolder holder = (ViewHolder) v.getTag();
						MyArtist artist = holder.getArtist();
						mListener.onLibraryArtistItemClick(artist, artists);
					}
				});
				tv_name = (TextView) convertView
						.findViewById(R.id.artist_name);
				tv_totalSongs = (TextView) convertView
						.findViewById(R.id.artist_totalSongs);
				holder = new ViewHolder(artist, tv_name, tv_totalSongs);
			} else {
				holder = (ViewHolder) convertView.getTag();
				tv_name = holder.getTv_name();
				tv_totalSongs = holder.getTv_totalSongs();
				holder.setArtist(artist);
			}
			tv_name.setText(artist.getName());
			tv_totalSongs.setText(artist.getSongs().size()+" [songs]");
			convertView.setTag(holder);
			return convertView;
		}
	}

	private MyLibraryArtistItemClickListener mListener = null;
	public ArrayList<MyArtist> artists = null;

	public MyLibraryArtistsFragment(){	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Bundle data = getArguments();
		artists = ((_MyLibaryDataProvider)getActivity()).getArtists();
		if(artists==null)
		{
			artists = new ArrayList<MyArtist>();//by default
		}
		
		MyLibraryArtistsAdapter adp = new MyLibraryArtistsAdapter(getActivity().getApplicationContext(),
				R.layout.library_artists_listview_item, artists, new MyLibraryArtistItemClickListener() {

					@Override
					public void onLibraryArtistItemClick(MyArtist current,
							ArrayList<MyArtist> artists_) {
						mListener.onLibraryArtistItemClick(current, artists_);
					}

					
					
				});
		
		setListAdapter(adp);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		mListener = (MyLibraryArtistItemClickListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.library_artists_listview, container, false);
		return v;
	}
}
