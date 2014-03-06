package com.example.hour16app2;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridCursorAdapter extends CursorAdapter {
	Context mContext;
	ViewGroup mViewGroup;
	private Boolean flinging = false;
	
	public GridCursorAdapter(Context context, Cursor c, int flags, ViewGroup viewGroup) {
		super(context, c, flags);
		mContext = context;
		mViewGroup = viewGroup;
		Log.w("qd", "Init GridCursorAdapter...");
	}

	public Boolean getFlinging() {
		return flinging;
	}
	public void setFlinging(Boolean flinging) {
		this.flinging = flinging;
	}	
	@Override
	public void bindView(View v, Context context, Cursor c) {
		Log.w("qd", "bindView started on viewid="+v.getId());
		GridViewHolder vh = (GridViewHolder) v.getTag();
		if (vh == null) {
			vh = new GridViewHolder();
			vh.photoImageView =  (ImageView) v.findViewById(R.id.photoImageView);
			vh.photoTitleView =  (TextView) v.findViewById(R.id.photoTitleView);
		}		
		if (!flinging){
			Log.w("qd", "Not flinging...");
			InstagramPhoto currentPhoto = InstagramPhotoDbAdapter.getPhotoFromCursor(c);
			vh.photoImageView.setImageResource(R.drawable.imageholder);
			vh.id = currentPhoto.id;
			v.setTag(vh);
			vh.photoImageView.setTag(currentPhoto.id);
			vh.photoTitleView.setTag(currentPhoto.id+"_title");//importance
//			LoadBitmapTask task = new LoadBitmapTask(mContext, mViewGroup, currentPhoto, vh.photoImageView);

			LoadBitmapTask task = new LoadBitmapTask(mContext, mViewGroup, currentPhoto);
			task.execute();
		}else{
			Log.w("qd", "Flinging...");
			vh.photoImageView.setImageResource(R.drawable.imageholder);
		}

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		View v = li.inflate(R.layout.grid_item, parent, false);
		return (v);

	}
	static class GridViewHolder {
		String id;
		ImageView photoImageView;
		TextView photoTitleView;
	}
	

}


