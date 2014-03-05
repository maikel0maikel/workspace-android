package com.example.hour15app;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
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
	qdAdapter mAdapter;
	Gallery mGallery;
	
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
    	mAdapter=new qdAdapter(getActivity().getApplicationContext(), null);
    	mGallery.setAdapter(mAdapter);
    	
		getLoaderManager().initLoader(0, null, this);
    	return mGallery;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	
    }
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CursorLoader(getActivity(),
				Uri.withAppendedPath(InstagramContentProvider.CONTENT_URI, "favourite/"+MainActivity.limit_r)
				, null, null, null, null);
	}
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(arg1);
	}
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);
	}
	class qdAdapter extends BaseAdapter
	{
		Context mCTX;
		LayoutInflater mINF;
		String chuoi[] = {};
		public qdAdapter(Context ctx, Cursor in) {
			// TODO Auto-generated constructor stub
			mCTX=ctx;
			data=in;
			mINF = (LayoutInflater)mCTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		Cursor data=null;
		public void swapCursor(Cursor in)
		{
			data=in;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data==null?0:data.getCount();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			if(data==null || data.getCount()==0) return null;
			if(data.moveToPosition(arg0))
			{
				InstagramPhoto hk=InstagramPhotoDbAdapter.getPhotoFromCursor(data);//fail tai day
				//data.moveToFirst();
				return hk;//new InstagramPhoto();
			}
			return new InstagramPhoto();
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			TextView tmpv=(TextView)arg1;
			if(arg1==null)
			{
			tmpv = (TextView)mINF.inflate(R.layout.gallery_item, null);
			tmpv.setText(((InstagramPhoto)getItem(arg0)).title + arg0);
			}
			else
			{
				//nothing to do right nơ right here, kuể 
			}
			return tmpv;
		}
		
	}
}
