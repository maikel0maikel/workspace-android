package com.example.hour16app2;

import com.example.hour16app2.GridCursorAdapter.GridViewHolder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
public class PhotoGridFragment extends Fragment implements LoaderCallbacks<Cursor>   {
	GridView mGrid;
	GridCursorAdapter mAdapter;
 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle b = this.getArguments();
		getLoaderManager().initLoader(0, b, this);
		mAdapter = new GridCursorAdapter(getActivity(), null,0, mGrid);
		mGrid.setAdapter(mAdapter);
		mGrid.setOnItemClickListener(new OnItemClickListener() {

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mGrid  = (GridView) inflater.inflate(R.layout.grid_fragment, container, false);
		mGrid.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
		        case OnScrollListener.SCROLL_STATE_IDLE:
		        	if (mAdapter!=null){
		        		if (mAdapter.getFlinging()){
		        			mAdapter.setFlinging(false);
			        		mAdapter.notifyDataSetChanged();	
		        		}			        		
		        	}
		            break;
		        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		            break;
		        case OnScrollListener.SCROLL_STATE_FLING:
		        	if (mAdapter!=null)
		        		mAdapter.setFlinging(true);
		            break;
		    }
				
			}
		});
		
		return mGrid;
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
