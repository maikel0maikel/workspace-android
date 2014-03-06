package com.example.hour16app2;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



class LoadBitmapTask extends AsyncTask {
	InstagramPhoto mPhoto;
	Handler mHandler = new Handler();
	Context mContext;
	ViewGroup mViewGroup;
		public LoadBitmapTask(Context context, ViewGroup viewGroup, InstagramPhoto photo) {

		this.mPhoto = photo;
		this.mContext = context;
		this.mViewGroup = viewGroup;
		Log.w("qd", "LoadBitmapTask Asyntask init...");
	} 

	// Decode image in background.
	@Override
	protected Object doInBackground(Object... params) {
		InputStream is;
		try {
			is =  mContext.getContentResolver().openInputStream(Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI ,mPhoto.id));
			Bitmap bitmap =  BitmapFactory.decodeStream(is);
			return(bitmap);
		} catch (FileNotFoundException e) {
			Log.w("qd", "IMG is temporary unavailable right now, please wait...");
			mContext.getContentResolver().registerContentObserver(Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI ,mPhoto.id), 
					 false, new PhotoContentObserver(mContext, mViewGroup, mHandler, mPhoto.id));
			Log.w("qd", "PhotoContentObserver registered with uri="+Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI ,mPhoto.id).getPath());
			return null;
		}
	}

	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Object bitmap) {
			if ( bitmap != null) {
			ImageView imageViewByTag = (ImageView) mViewGroup.findViewWithTag(mPhoto.id);
			TextView titleViewByTag = (TextView) mViewGroup.findViewWithTag(mPhoto.id+"_title");
			if (imageViewByTag != null) {
				imageViewByTag.setImageBitmap((Bitmap) bitmap);
				titleViewByTag.setText(
						mPhoto.title.substring(
								0,
								(20>mPhoto.title.length()?mPhoto.title.length()-1:20))
								+(20>mPhoto.title.length()?"":"..."));//maximum 20 char for title
				Log.w("qd", "IMG loaded to ImageView...");
			}
		}
	}

}
class PhotoContentObserver extends ContentObserver { 
	String mId;
	Context mContext;
	ViewGroup mViewGroup;
	
	public PhotoContentObserver(Context context, ViewGroup viewGroup, Handler handler, String id) { 
		super(handler); 
		mId = id;
		mContext = context;
		this.mViewGroup = viewGroup;
	}

	@Override
	public void onChange(boolean selfChange) {
		InputStream is;
		try {
			Log.w("qd", "PhotoContentObserver detected changed...");
			Log.w("qd", "Call when DownloadImageTask had been finished and make notify URI registered before...");
			//Call when DownloadImageTask had been finished...
			//Retreive byte stream again
			is = mContext.getContentResolver().openInputStream(Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI ,mId));
			Bitmap bitmap =  BitmapFactory.decodeStream(is);
			ImageView imageViewByTag = (ImageView) mViewGroup.findViewWithTag(mId);
			if (imageViewByTag != null) {
				imageViewByTag.setImageBitmap(bitmap);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.w("qd", "File still not found ERROR!@#$%^&...");
		}
	}
}