package com.bffmedia.hour15app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;



import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	ProgressBar mProgressBar;
	Tab mListTab;
	Tab mGridTab;
	
	private ArrayList<FlickrPhoto> mPhotos = new ArrayList<FlickrPhoto>();

	public final static String API_KEY ="PUT_YOUR_API_KEY_HERE";
	public final static String NUM_PHOTOS ="12";
	private static final String TAG = MainActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();	
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mListTab= actionBar.newTab().setText("List").setTabListener(new NavTabListener());
		actionBar.addTab(mListTab);
		mGridTab= actionBar.newTab().setText("Grid").setTabListener(new NavTabListener());
		actionBar.addTab(mGridTab);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		if (isOnline()){
			LoadPhotos task = new LoadPhotos();
			task.execute();
		}else{
			mProgressBar.setVisibility(View.GONE);
			Toast.makeText(MainActivity.this.getApplicationContext(), "Please connect to retrieve photos", Toast.LENGTH_SHORT).show();
		}
	
		
	}

	public ArrayList<FlickrPhoto> getPhotos() {
		return mPhotos;
	}

	public void setPhotos(ArrayList<FlickrPhoto> mPhotos) {
		this.mPhotos = mPhotos;
	}
	
	public boolean isOnline() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) 
	            getSystemService(this.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void showList(){
		PhotoListFragment photoListFragment = new PhotoListFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.layout_container, photoListFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
	public void showGrid(){
		PhotoGridFragment photoGridFragment = new PhotoGridFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.layout_container, photoGridFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
	
	private class NavTabListener implements ActionBar.TabListener {
		public NavTabListener() {
		}
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (tab.equals(mListTab)){
				showList();
			}else{
				showGrid();
			}
		}
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}
	}
	
	private class LoadPhotos extends AsyncTask<String , String , Long > {
		

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Long result) {
			if (result==0){
				showList();
			}else{
				Toast.makeText(MainActivity.this.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
			}
			mProgressBar.setVisibility(View.GONE);
		}

		@Override
		protected Long doInBackground(String... params) {
			HttpURLConnection connection = null;
			FlickrPhotoDbAdapter photoDbAdapter = new FlickrPhotoDbAdapter(MainActivity.this);
			photoDbAdapter.open();
			try {
				URL dataUrl = new URL("http://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key="+ API_KEY+ "&per_page=" + NUM_PHOTOS +"&format=json&nojsoncallback=1");
				connection = (HttpURLConnection) dataUrl.openConnection();
				connection.connect();
				int status = connection.getResponseCode();
				Log.d("connection", "status " + status);
				if (status ==200){
				  InputStream is = connection.getInputStream();
				  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				  String responseString;
				  StringBuilder sb = new StringBuilder();
				  while ((responseString = reader.readLine()) != null) {
					  sb = sb.append(responseString);
				  }
				  String photoData = sb.toString();
				  mPhotos = FlickrPhoto.makePhotoList(photoData);
				  for (FlickrPhoto currentPhoto : mPhotos) {
					  ContentValues newValues = new ContentValues();
						if (currentPhoto.id!=null)
							newValues.put("flickr_id", currentPhoto.id);	
						if (currentPhoto.owner!=null)
							newValues.put("owner", currentPhoto.owner);	
						if (currentPhoto.secret!=null)
							newValues.put("secret", currentPhoto.secret);	
						if (currentPhoto.server!=null)
							newValues.put("server", currentPhoto.server);	
						if (currentPhoto.farm!=null)
							newValues.put("farm", currentPhoto.farm);	
						if (currentPhoto.title!=null)
							newValues.put("title", currentPhoto.title);	
						if (currentPhoto.isPublic!=null)
							newValues.put("isPublic", currentPhoto.isPublic);	
						if (currentPhoto.isFriend!=null)
							newValues.put("isFriend", currentPhoto.isFriend);	
						if (currentPhoto.isFamily!=null)
							newValues.put("isFamily", currentPhoto.isFamily);	
						if (currentPhoto.isFavorite!=null)
							newValues.put("isFavorite", currentPhoto.isFavorite);		
						
				
						Cursor photoCursor = managedQuery(Uri.withAppendedPath(FlickrPhotoProvider.CONTENT_URI ,currentPhoto.id), null, null, null, null);
						if (photoCursor.moveToFirst()){
							 FlickrPhoto existingPhoto = FlickrPhotoDbAdapter.getPhotoFromCursor(photoCursor);
							 photoDbAdapter.updatePhoto(existingPhoto.id, currentPhoto);
							 Log.d(TAG,"Updated " + existingPhoto.id);
							 photoCursor.close();
						}else{
							Uri newUri = getContentResolver().insert(
									FlickrPhotoProvider.CONTENT_URI, 
									newValues);   
						}
	
					} 
				  
				  Log.d("connection", photoData);
				  return (0l);
				}else{
					return (1l);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return (1l);
			} 
			catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
				return (1l);
			} catch (NullPointerException e) {
				e.printStackTrace();
				return (1l);
			} catch (JSONException e) {
				e.printStackTrace();
				return (1l);
			} finally {
				connection.disconnect();
				photoDbAdapter.close();
			}
		}
	}

}
