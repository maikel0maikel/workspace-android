package com.bffmedia.hour15app;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
public class PhotoGridFragment extends Fragment implements LoaderCallbacks<Cursor>   {
	GridView mGrid;
	SimpleCursorAdapter mAdapter;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
		mAdapter = new SimpleCursorAdapter(getActivity(), 
				android.R.layout.simple_list_item_1, 
				null,
				new String[] {"title"},
				new int[] { android.R.id.text1 }, 0); 
		mGrid.setAdapter(mAdapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mGrid  = (GridView) inflater.inflate(R.layout.grid_fragment, container, false);
		return mGrid;
	}
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				FlickrPhotoProvider.CONTENT_URI, 
				null, null, null, null);		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);

		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);		
	}
	
} 
