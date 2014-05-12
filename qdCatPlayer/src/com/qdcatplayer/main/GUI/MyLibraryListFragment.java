package com.qdcatplayer.main.GUI;

import java.util.HashMap;

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

public class MyLibraryListFragment extends ListFragment {
	public interface MyItemClickListener {
		public void onClick(int itemType);
	}

	private class MyLibraryAdapter extends ArrayAdapter<String> {
		private class ViewHolder {
			private ImageView img = null;
			private TextView tv = null;
			private String itemId = null;

			public String getItemId() {
				return itemId;
			}

			public void setItemId(String itemId) {
				this.itemId = itemId;
			}

			public ViewHolder(ImageView img, TextView tv, String itemId) {
				this.img = img;
				this.tv = tv;
				this.itemId = itemId;
			}

			public ImageView getImg() {
				return img;
			}

			public TextView getTv() {
				return tv;
			}

			public void setImg(ImageView img) {
				this.img = img;
			}

			public void setTv(TextView tv) {
				this.tv = tv;
			}
		}

		private String[] items = null;
		private MyLibraryClickListener mListener = null;

		public MyLibraryAdapter(Context context, int textViewResourceId,
				String[] objects, MyLibraryClickListener listener) {

			super(context, textViewResourceId, objects);
			items = objects;
			mListener = listener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView img;
			TextView tv;
			ViewHolder holder;
			if (convertView == null) {
				/*
				 * LayoutInflater inflater = ((Activity) getContext())
				 * .getLayoutInflater();
				 */
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.library_listview_item,
						parent, false);
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewHolder holder = (ViewHolder) v.getTag();
						mListener.onLibraryItemClick(holder.getItemId());
					}
				});
				img = (ImageView) convertView
						.findViewById(R.id.library_item_imageView);
				tv = (TextView) convertView
						.findViewById(R.id.library_item_textView);
				holder = new ViewHolder(img, tv, ids[position]);
			} else {
				holder = (ViewHolder) convertView.getTag();
				img = holder.getImg();
				tv = holder.getTv();
			}
			// set value
			tv.setText(items[position]);

			convertView.setTag(holder);
			return convertView;
		}
	}

	public interface MyLibraryClickListener {
		public void onLibraryItemClick(String itemId);
	}

	public String[] ids = null;
	public HashMap<String, String> map = null;
	private MyLibraryClickListener mListener = null;
	public String[] values = null;

	public MyLibraryListFragment() {

	}

	private void loadResource() {
		ids = getResources().getStringArray(R.array.library_item_id_array);
		values = getResources()
				.getStringArray(R.array.library_item_value_array);

		map = new HashMap<String, String>();
		for (int i = 0; i < ids.length; i++) {
			map.put(ids[i], values[i]);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// load resource
		loadResource();
		MyLibraryAdapter adp = new MyLibraryAdapter(getActivity()
				.getApplicationContext(), R.layout.library_listview_item,
				values, new MyLibraryClickListener() {

					@Override
					public void onLibraryItemClick(String itemId) {
						// send message to parent activity
						mListener.onLibraryItemClick(itemId);
					}
				});

		setListAdapter(adp);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		mListener = (MyLibraryClickListener) activity;
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
