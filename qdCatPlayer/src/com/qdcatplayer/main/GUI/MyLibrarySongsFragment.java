package com.qdcatplayer.main.GUI;

import java.util.ArrayList;
import java.util.HashMap;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DAOs.MySource;
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
 * Set songs (ArrayList<MySong>) over bundle by
 * using bundle.setSerializable(MyLibrarySongsFragment.SONGS, songs)
 * @author admin
 *
 */
public class MyLibrarySongsFragment extends ListFragment {
	public static String SONGS = "SONGS";
	public interface MyLibrarySongItemClickListener {
		public void onLibrarySongItemClick(MySong current, ArrayList<MySong> playlist);
	}

	private class MyLibrarySongsAdapter extends ArrayAdapter<MySong> {
		private class ViewHolder {
			private MySong song = null;
			private TextView tv_album = null;
			private TextView tv_artist = null;
			private TextView tv_duration = null;
			private TextView tv_title = null;

			public ViewHolder(MySong song, TextView tv_title,
					TextView tv_artist, TextView tv_duration,TextView tv_album) {
				this.tv_title = tv_title;
				this.tv_artist= tv_artist;
				this.tv_duration = tv_duration;
				this.tv_album = tv_album;
				this.song = song;
			}

			public MySong getSong() {
				return song;
			}

			public TextView getTv_album() {
				return tv_album;
			}

			public TextView getTv_artist() {
				return tv_artist;
			}

			public TextView getTv_duration() {
				return tv_duration;
			}

			public TextView getTv_title() {
				return tv_title;
			}

			public void setSong(MySong song) {
				this.song = song;
			}

			public void setTv_album(TextView tv_album) {
				this.tv_album = tv_album;
			}

			public void setTv_artist(TextView tv_artist) {
				this.tv_artist = tv_artist;
			}

			public void setTv_duration(TextView tv_duration) {
				this.tv_duration = tv_duration;
			}

			public void setTv_title(TextView tv_title) {
				this.tv_title = tv_title;
			}

		}

		private MyLibrarySongItemClickListener mListener = null;
		private ArrayList<MySong> songs = null;

		public MyLibrarySongsAdapter(Context context, int textViewResourceId,
				ArrayList<MySong> objects, MyLibrarySongItemClickListener listener) {

			super(context, textViewResourceId, objects);
			songs = objects;
			mListener = listener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv_title;
			TextView tv_artist;
			TextView tv_duration;
			TextView tv_album;
			ViewHolder holder;
			MySong song = songs.get(position);
			if (convertView == null) {
				/*LayoutInflater inflater = ((Activity) getContext())
						.getLayoutInflater();*/
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.library_songs_listview_item,
						parent, false);
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolder holder = (ViewHolder) v.getTag();
						MySong song = holder.getSong();
						mListener.onLibrarySongItemClick(song, songs);
					}
				});
				tv_title = (TextView) convertView
						.findViewById(R.id.song_title);
				tv_artist = (TextView) convertView
						.findViewById(R.id.song_artist);
				tv_duration = (TextView) convertView
						.findViewById(R.id.song_duration);
				tv_album = (TextView) convertView
						.findViewById(R.id.song_album);
				holder = new ViewHolder(song, tv_title,  tv_artist, tv_duration, tv_album);
			} else {
				holder = (ViewHolder) convertView.getTag();
				tv_title = holder.getTv_title();
				tv_album = holder.getTv_album();
				tv_artist = holder.getTv_artist();
				tv_duration = holder.getTv_duration();
				holder.setSong(song);
			}
			tv_title.setText(song.getTitle());
			tv_album.setText(song.getAlbum()==null?"unknown":song.getAlbum().getName());
			tv_artist.setText(song.getArtist()==null?"unknown":song.getArtist().getName());
			tv_duration.setText(song.getDuration().toString());

			convertView.setTag(holder);
			return convertView;
		}
	}

	private MyLibrarySongItemClickListener mListener = null;
	public ArrayList<MySong> songs = null;

	public MyLibrarySongsFragment(){	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Bundle data = getArguments();
		songs = ((_MyLibaryDataProvider)getActivity()).getSongs();
		if(songs==null)
		{
			songs = new ArrayList<MySong>();//by default
		}
		
		MyLibrarySongsAdapter adp = new MyLibrarySongsAdapter(getActivity().getApplicationContext(),
				R.layout.library_songs_listview_item, songs, new MyLibrarySongItemClickListener() {
					@Override
					public void onLibrarySongItemClick(MySong current, ArrayList<MySong> playlist) {
						// TODO Auto-generated method stub
						mListener.onLibrarySongItemClick(current, playlist);
					}
				});
		
		setListAdapter(adp);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		mListener = (MyLibrarySongItemClickListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.library_songs_listview, container, false);
		return v;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);//not fail here
	}
	
}
