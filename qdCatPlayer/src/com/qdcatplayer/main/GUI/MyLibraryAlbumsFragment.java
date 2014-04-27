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
import android.widget.ImageView;
import android.widget.TextView;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MyAlbum;

/**
 * Set albums (ArrayList<MyAlbum>) over bundle by
 * using bundle.setSerializable(MyLibraryAlbumsFragment.SONGS, albums)
 * @author admin
 *
 */
public class MyLibraryAlbumsFragment extends ListFragment {
	public static String ALBUMS = "ALBUMS";
	public interface MyLibraryAlbumItemClickListener {
		public void onLibraryAlbumItemClick(MyAlbum current, ArrayList<MyAlbum> albums);
	}

	private class MyLibraryAlbumsAdapter extends ArrayAdapter<MyAlbum> {
		private class ViewHolder {
			private MyAlbum album = null;
			private TextView tv_name = null;
			private TextView tv_artist = null;
			private TextView tv_totalSongs = null;
			private ImageView img = null;

			public ViewHolder(MyAlbum album, TextView tv_name,
					TextView tv_artist, TextView tv_totalSongs,ImageView img) {
				this.tv_name = tv_name;
				this.tv_artist= tv_artist;
				this.tv_totalSongs = tv_totalSongs;
				this.img = img;
				this.album = album;
				
			}

			public MyAlbum getAlbum() {
				return album;
			}

			public void setAlbum(MyAlbum album) {
				this.album = album;
			}

			public TextView getTv_name() {
				return tv_name;
			}

			public void setTv_name(TextView tv_name) {
				this.tv_name = tv_name;
			}

			public TextView getTv_artist() {
				return tv_artist;
			}

			public void setTv_artist(TextView tv_artist) {
				this.tv_artist = tv_artist;
			}

			public TextView getTv_totalSongs() {
				return tv_totalSongs;
			}

			public void setTv_totalSongs(TextView tv_totalSongs) {
				this.tv_totalSongs = tv_totalSongs;
			}

			public ImageView getImg() {
				return img;
			}

			public void setImg(ImageView img) {
				this.img = img;
			}


		}

		private MyLibraryAlbumItemClickListener mListener = null;
		private ArrayList<MyAlbum> albums = null;

		public MyLibraryAlbumsAdapter(Context context, int textViewResourceId,
				ArrayList<MyAlbum> objects, MyLibraryAlbumItemClickListener listener) {

			super(context, textViewResourceId, objects);
			albums = objects;
			mListener = listener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv_name;
			TextView tv_artist;
			TextView tv_totalSongs;
			ImageView img;
			ViewHolder holder;
			MyAlbum album = albums.get(position);
			if (convertView == null) {
				/*LayoutInflater inflater = ((Activity) getContext())
						.getLayoutInflater();*/
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.library_albums_listview_item,
						parent, false);
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolder holder = (ViewHolder) v.getTag();
						MyAlbum album = holder.getAlbum();
						mListener.onLibraryAlbumItemClick(album, albums);
					}
				});
				tv_name = (TextView) convertView
						.findViewById(R.id.album_name);
				tv_artist = (TextView) convertView
						.findViewById(R.id.album_artist);
				tv_totalSongs = (TextView) convertView
						.findViewById(R.id.album_totalSongs);
				img = (ImageView) convertView
						.findViewById(R.id.album_image);
				holder = new ViewHolder(album, tv_name,  tv_artist, tv_totalSongs, img);
			} else {
				holder = (ViewHolder) convertView.getTag();
				tv_name = holder.getTv_name();
				img = holder.getImg();
				tv_artist = holder.getTv_artist();
				tv_totalSongs = holder.getTv_totalSongs();
				holder.setAlbum(album);
			}
			tv_name.setText(album.getName());
			tv_totalSongs.setText(album.getSongs().size()+" [songs]");
			tv_artist.setText("[artist]");
			convertView.setTag(holder);
			return convertView;
		}
	}

	private MyLibraryAlbumItemClickListener mListener = null;
	public ArrayList<MyAlbum> albums = null;

	public MyLibraryAlbumsFragment(){	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		albums = ((_MyLibaryDataProvider)getActivity()).getAlbums();
		if(albums==null)
		{
			albums = new ArrayList<MyAlbum>();//by default
		}
		
		MyLibraryAlbumsAdapter adp = new MyLibraryAlbumsAdapter(getActivity().getApplicationContext(),
				R.layout.library_albums_listview_item, albums, new MyLibraryAlbumItemClickListener() {

					@Override
					public void onLibraryAlbumItemClick(MyAlbum current,
							ArrayList<MyAlbum> albums_) {
						mListener.onLibraryAlbumItemClick(current, albums_);
					}
					
				});
		
		setListAdapter(adp);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		mListener = (MyLibraryAlbumItemClickListener) activity;
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
		super.onSaveInstanceState(outState);
	}
}
