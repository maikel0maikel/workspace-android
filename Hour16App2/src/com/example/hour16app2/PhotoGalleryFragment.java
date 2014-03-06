package com.example.hour16app2;
import com.example.hour16app2.GridCursorAdapter.GridViewHolder;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
 
public class PhotoGalleryFragment extends Fragment implements LoaderCallbacks<Cursor> {
	GridCursorAdapter mAdapter;
	Gallery mGallery;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Bundle b = this.getArguments();
		getLoaderManager().initLoader(0, b, this);
		mAdapter = new GridCursorAdapter(getActivity(), null,0, mGallery);
		mGallery.setAdapter(mAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				GridViewHolder vh = (GridViewHolder) v.getTag();
				ImageViewFragment instagramImageFragment = new ImageViewFragment();
				Bundle args = new Bundle();   
				args.putString("PHOTO_ID", vh.id);
				instagramImageFragment.setArguments(args);
		  		FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
					ft.replace(R.id.layout_container, instagramImageFragment);
					ft.addToBackStack("Image");
					ft.commit();				
			}
			

		});
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	mGallery= (Gallery) inflater.inflate(R.layout.gallery_layout, container, false);
		
    	/*
    	// TODO Auto-generated method stub
    	mGallery.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    				long arg3) {
    			// TODO Auto-generated method stub
    			Toast.makeText(getActivity().getApplicationContext(), "vd", 2000).show();
    		}
		});
		*/
    	return mGallery;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	
    }
    @Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		boolean showAll = true;
		CursorLoader cursorLoader;
		if (args!=null){
			showAll = args.getBoolean("ALL", true);
		}
		if (showAll){
			 cursorLoader = new CursorLoader(getActivity(),
					InstagramContentProvider.CONTENT_URI, 
					null, null, null, null);		
		}else{
			 cursorLoader = new CursorLoader(getActivity(),
					 InstagramContentProvider.CONTENT_URI, 
					null, "is_favourite='1'", null, null);	
		}
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		int t = cursor.getColumnCount();
		t = cursor.getCount();//OK
		mAdapter.swapCursor(cursor);

		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);		
	}
}
