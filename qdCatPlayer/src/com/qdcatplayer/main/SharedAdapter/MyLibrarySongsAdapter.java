package com.qdcatplayer.main.SharedAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.GUI.MainPlayerFragment.MyMainPLayerDataProvider;
import com.qdcatplayer.main.GUI.MyLibrarySongsFragment.MyLibrarySongItemClickListener;
import com.qdcatplayer.main.Libraries.PlayerUtilities;

public class MyLibrarySongsAdapter extends ArrayAdapter<MySong> {
	private class ViewHolder {
		private MySong song = null;
		private TextView tv_album = null;
		private TextView tv_artist = null;
		private TextView tv_duration = null;
		private TextView tv_title = null;

		public ViewHolder(MySong song, TextView tv_title, TextView tv_artist,
				TextView tv_duration, TextView tv_album) {
			this.tv_title = tv_title;
			this.tv_artist = tv_artist;
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
	private MyMainPLayerDataProvider dataProvider = null;

	public MyLibrarySongsAdapter(Context context, int textViewResourceId,
			ArrayList<MySong> objects, MyLibrarySongItemClickListener listener,
			MyMainPLayerDataProvider dataProvider) {

		super(context, textViewResourceId, objects);
		songs = objects;
		mListener = listener;
		this.dataProvider = dataProvider;
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
			/*
			 * LayoutInflater inflater = ((Activity) getContext())
			 * .getLayoutInflater();
			 */
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.library_songs_listview_item, parent, false);

			/*
			 * Trung tâm bắt sự kiện đầu tiên
			 */
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ViewHolder holder = (ViewHolder) v.getTag();
					MySong song = holder.getSong();
					mListener.onLibrarySongItemClick(song, songs);
				}
			});
			convertView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					ViewHolder holder = (ViewHolder) v.getTag();
					MySong song = holder.getSong();
					mListener.onLibrarySongItemLongClick(song, songs);
					return true;
				}
			});

			tv_title = (TextView) convertView.findViewById(R.id.song_title);
			tv_artist = (TextView) convertView.findViewById(R.id.song_artist);
			tv_duration = (TextView) convertView
					.findViewById(R.id.song_duration);
			tv_album = (TextView) convertView.findViewById(R.id.song_album);
			holder = new ViewHolder(song, tv_title, tv_artist, tv_duration,
					tv_album);
		} else {
			holder = (ViewHolder) convertView.getTag();
			tv_title = holder.getTv_title();
			tv_album = holder.getTv_album();
			tv_artist = holder.getTv_artist();
			tv_duration = holder.getTv_duration();
			holder.setSong(song);
		}
		tv_title.setText(song.getTitle());
		tv_album.setText(song.getAlbum() == null ? "unknown" : song.getAlbum()
				.getName());
		tv_artist.setText(song.getArtist() == null ? "unknown" : song
				.getArtist().getName());
		tv_duration.setText(PlayerUtilities.milliSecondsToTimer(song
				.getDuration()));

		// begin: doi color baclground khi dang la curent playing song
		if (dataProvider != null
				&& dataProvider.getCurrentSong()!=null
				&& dataProvider.getCurrentSong().getPath().getAbsPath()
						.equals(song.getPath().getAbsPath())) {
			// when current playing
			convertView.setBackgroundColor(getContext().getResources()
					.getColor(R.color.library_activeSong_bgColor));
		} else {
			// by default
			convertView
					.setBackgroundResource(android.R.drawable.list_selector_background);
		}
		// end
		convertView.setTag(holder);
		return convertView;
	}
}
