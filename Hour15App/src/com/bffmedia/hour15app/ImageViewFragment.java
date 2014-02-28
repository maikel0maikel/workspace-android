package com.bffmedia.hour15app;

import java.io.FileNotFoundException;
import java.io.InputStream;



import android.app.Fragment;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImageViewFragment extends Fragment   {
	ImageView mImageView;
	String mPhotoId=null;
	Bitmap mBitmap;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle b = this.getArguments();		
		if (b!=null){  
			mPhotoId=b.getString("PHOTO_ID");	
		}
		Handler handler = new Handler();		
	    InputStream is;
		try {
			is = getActivity().getContentResolver().openInputStream(Uri.withAppendedPath(FlickrPhotoProvider.CONTENT_URI ,mPhotoId));
			mBitmap =  BitmapFactory.decodeStream(is);
			mImageView.setImageBitmap(mBitmap);
		} catch (FileNotFoundException e) {
			getActivity().getContentResolver().registerContentObserver(Uri.withAppendedPath(FlickrPhotoProvider.CONTENT_URI ,mPhotoId), false, new PhotoContentObserver(handler)); 
		}	
	}

	class PhotoContentObserver extends ContentObserver { 
	    public PhotoContentObserver(Handler handler) { 
	        super(handler); 
	    }
	    
	    @Override
	    public void onChange(boolean selfChange) {
	        InputStream is;
	        try {
	        	if (ImageViewFragment.this.isAdded()){
	        		is = getActivity().getContentResolver().openInputStream(Uri.withAppendedPath(FlickrPhotoProvider.CONTENT_URI ,mPhotoId));
	        		mBitmap =  BitmapFactory.decodeStream(is);
					mImageView.setImageBitmap(mBitmap);
	        	}
			} catch (FileNotFoundException e) {
			}
	    }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mImageView  = (ImageView) inflater.inflate(R.layout.image_fragment, container, false);
		return mImageView;
	}
}
