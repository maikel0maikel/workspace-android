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
import com.qdcatplayer.main.Entities.MyFolder;

/**
 * Set folder (ArrayList<MyFolder>) over bundle by using
 * bundle.setSerializable(MyLibraryFoldersFragment.FOLDERS, folders)
 * 
 * @author admin
 * 
 */
public class MyLibraryFoldersFragment extends ListFragment {
	public static String FOLDERS = "FOLDERS";

	public interface MyLibraryFolderItemClickListener {
		public void onLibraryFolderItemClick(MyFolder current,
				ArrayList<MyFolder> folders);
		public void onLibraryFolderItemLongClick(MyFolder current,
				ArrayList<MyFolder> folders);
	}

	private class MyLibraryFoldersAdapter extends ArrayAdapter<MyFolder> {
		private class ViewHolder {
			private MyFolder folder = null;
			private TextView tv_name = null;
			private ImageView img = null;
			public ImageView getImg() {
				return img;
			}

			public void setImg(ImageView img) {
				this.img = img;
			}

			private TextView tv_absPath = null;
			private TextView tv_totalSongs = null;

			public ViewHolder(MyFolder folder, TextView tv_name,
					TextView tv_absPath, TextView tv_totalSongs, ImageView img) {
				this.tv_name = tv_name;
				this.tv_absPath = tv_absPath;
				this.tv_totalSongs = tv_totalSongs;
				this.folder = folder;
				this.img=img;
			}

			public MyFolder getFolder() {
				return folder;
			}

			public void setFolder(MyFolder folder) {
				this.folder = folder;
			}

			public TextView getTv_name() {
				return tv_name;
			}

			public void setTv_name(TextView tv_name) {
				this.tv_name = tv_name;
			}

			public TextView getTv_absPath() {
				return tv_absPath;
			}

			public void setTv_absPath(TextView tv_absPath) {
				this.tv_absPath = tv_absPath;
			}

			public TextView getTv_totalSongs() {
				return tv_totalSongs;
			}

			public void setTv_totalSongs(TextView tv_totalSongs) {
				this.tv_totalSongs = tv_totalSongs;
			}
		}

		private MyLibraryFolderItemClickListener mListener = null;
		private ArrayList<MyFolder> folders = null;

		public MyLibraryFoldersAdapter(Context context, int textViewResourceId,
				ArrayList<MyFolder> objects,
				MyLibraryFolderItemClickListener listener) {

			super(context, textViewResourceId, objects);
			folders = objects;
			mListener = listener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv_name;
			TextView tv_absPath;
			TextView tv_totalSongs;
			ViewHolder holder;
			ImageView img;
			MyFolder folder = folders.get(position);
			if (convertView == null) {
				/*
				 * LayoutInflater inflater = ((Activity) getContext())
				 * .getLayoutInflater();
				 */
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.library_folders_listview_item, parent, false);
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolder holder = (ViewHolder) v.getTag();
						MyFolder current = holder.getFolder();
						mListener.onLibraryFolderItemClick(current, folders);
					}
				});
				convertView.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						ViewHolder holder = (ViewHolder) v.getTag();
						MyFolder current = holder.getFolder();
						mListener.onLibraryFolderItemLongClick(current, folders);
						return true;
					}
				});
				
				tv_name = (TextView) convertView.findViewById(R.id.folder_name);
				img = (ImageView) convertView.findViewById(R.id.folder_icon);
				tv_absPath = (TextView) convertView
						.findViewById(R.id.folder_absPath);
				tv_totalSongs = (TextView) convertView
						.findViewById(R.id.folder_totalSongs);

				holder = new ViewHolder(folder, tv_name, tv_absPath,
						tv_totalSongs, img);
			} else {
				holder = (ViewHolder) convertView.getTag();
				tv_name = holder.getTv_name();
				tv_absPath = holder.getTv_absPath();
				tv_totalSongs = holder.getTv_totalSongs();
				img=holder.getImg();
				holder.setFolder(folder);
			}
			tv_name.setText(folder.getFolderName());
			tv_totalSongs.setText(folder.getAllRecursiveSongs().size()
					+ " [songs]");
			tv_absPath.setText(folder.getAbsPath());
			//img.setImageResource(R.drawable.folder_icon);
			
			convertView.setTag(holder);
			return convertView;
		}
	}

	private MyLibraryFolderItemClickListener mListener = null;
	public ArrayList<MyFolder> folders = null;

	public MyLibraryFoldersFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		folders = ((_MyLibaryDataProvider) getActivity()).getFolders();
		if (folders == null) {
			folders = new ArrayList<MyFolder>();// by default
		}

		MyLibraryFoldersAdapter adp = new MyLibraryFoldersAdapter(getActivity()
				.getApplicationContext(),
				R.layout.library_folders_listview_item, folders,
				new MyLibraryFolderItemClickListener() {

					@Override
					public void onLibraryFolderItemClick(MyFolder current,
							ArrayList<MyFolder> folders_) {
						mListener.onLibraryFolderItemClick(current, folders_);
					}

					@Override
					public void onLibraryFolderItemLongClick(MyFolder current,
							ArrayList<MyFolder> folders) {
						mListener.onLibraryFolderItemLongClick(current, folders);
					}

				});

		setListAdapter(adp);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		mListener = (MyLibraryFolderItemClickListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.library_listview, container,
				false);// use common "list_content" layout
		return v;
	}
}
