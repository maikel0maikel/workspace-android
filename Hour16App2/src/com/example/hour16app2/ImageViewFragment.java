package com.example.hour16app2;

import java.io.FileNotFoundException;
import java.io.InputStream;




import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageViewFragment extends Fragment implements LoaderCallbacks<Cursor>  {
	ImageView mImageView;
	TextView mTitleView;
	Button mFavoriteButton;
	String mPhotoId=null;
	Bitmap mBitmap;
	InstagramPhoto mCurrentPhoto;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle b = this.getArguments();	
		getLoaderManager().initLoader(0, b, this);
		
	}
	//class observer rieng cho class nay
	class PhotoContentObserver extends ContentObserver { 
	    public PhotoContentObserver(Handler handler) { 
	        super(handler); 
	    }
	    
	    @Override
	    public void onChange(boolean selfChange) {
	        InputStream is;
	        try {
	        	if (ImageViewFragment.this.isAdded()){
	        		is = getActivity().getContentResolver().openInputStream(Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI ,mPhotoId));
	        		mBitmap =  BitmapFactory.decodeStream(is);
					mImageView.setImageBitmap(mBitmap);
	        	}
			} catch (FileNotFoundException e) {
			}
	    }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.image_fragment, container, false);
		mImageView  = (ImageView) v.findViewById(R.id.imageView);
		mTitleView  = (TextView) v.findViewById(R.id.titleTextView);
		mFavoriteButton  = (Button) v.findViewById(R.id.favoriteButton);
		mFavoriteButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Log.d ("image", "in onclick");
            	/*
        		 ContentValues newValues = new ContentValues();
					if (mCurrentPhoto.id!=null)
						newValues.put("instagram_id", mCurrentPhoto.id);	
						
					

			  		if (mCurrentPhoto.is_favourite==1){
			  			newValues.put("is_favourite", 0);
	        			mFavoriteButton.setText("Add to Favorites");
	        		}else{
	        			newValues.put("is_favourite", 1);
	        			mFavoriteButton.setText("Remove from Favorites");
	        		}
			  		int id = getActivity().getContentResolver().update(
							InstagramContentProvider.CONTENT_URI, 
							newValues, " instagram_id='"+mCurrentPhoto.id+"'", null);
			      	Log.d ("image", "result " + id);
			      	*/
            	if (mCurrentPhoto.is_favourite==1){
            		mCurrentPhoto.is_favourite=0;
            	}else
            	if (mCurrentPhoto.is_favourite==0){
            		mCurrentPhoto.is_favourite=1;
            	}
            	InstagramPhotoDbAdapter kkk=new InstagramPhotoDbAdapter(getActivity().getApplicationContext());
            	kkk.open();
            	kkk.updatePhoto(mCurrentPhoto.id, mCurrentPhoto);
			      	
            }
    });

		return v;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (args!=null){  
			mPhotoId=args.getString("PHOTO_ID");	
			CursorLoader cursorLoader = new CursorLoader(getActivity(),
					 Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI ,mPhotoId), 
					null, null, null, null);		
			int i=6;//qd for test
			return cursorLoader;
	    }
		return null;	 
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mCurrentPhoto = InstagramPhotoDbAdapter.getPhotoFromCursor(cursor);
		mTitleView.setText(mCurrentPhoto.title +"---"+mCurrentPhoto.id);
		if (mCurrentPhoto.is_favourite==1){
			mFavoriteButton.setText("Remove from Favorites");
		}else{
			mFavoriteButton.setText("Add to Favorites");
		}
		

		Handler handler = new Handler();		
	    InputStream is;
		try {
			is = getActivity().getContentResolver().openInputStream(Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI ,mPhotoId));
			mBitmap =  BitmapFactory.decodeStream(is);
			mImageView.setImageBitmap(mBitmap);
		} catch (FileNotFoundException e) {
			getActivity().getContentResolver().registerContentObserver(Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI ,mPhotoId), false, new PhotoContentObserver(handler)); 
		}	
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
	
}
