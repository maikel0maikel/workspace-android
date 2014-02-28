package com.example.hour14app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.hour14app.InstagramPhoto;

import com.example.hour14app.R;
import com.example.hour14app.R.id;
import com.example.hour14app.R.layout;
import com.example.hour14app.R.menu;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	ProgressBar mProgressBar;
	Tab mListTab;
	Tab mGridTab;
	ImageView img_view;
	private ArrayList<InstagramPhoto> mPhotos = new ArrayList<InstagramPhoto>();
	
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
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar_working);
		if (isOnline()){
			LoadPhotos task = new LoadPhotos();
			task.execute();
		}else{
			mProgressBar.setVisibility(View.GONE);
			Toast.makeText(MainActivity.this.getApplicationContext(), "Please connect to retrieve photos", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	//call by Asyntask when finish
	private void onGetJSON_Finish()
	{
		InstagramPhotoDbAdapter mDB = new InstagramPhotoDbAdapter(MainActivity.this);
		mDB.open();
		  for(InstagramPhoto item:mPhotos)
		  {
			  if(mDB.getPhotoByInstagramId(item.id)!=null)
			  {
				  //đã có trong CSDL
				  mDB.updatePhoto(item.id, item);
				  Log.w("qd","Updated Instagram photo id "+item.id+" in SQLite db");
			  }
			  else
			  {
				  //chưa có
				  mDB.createPhoto(item);
				  Log.w("qd","Inserted new Instagram photo id "+item.id+" to SQLite db");
				  //tải hình về
			  }
			  
		  }
		  mDB.close();
		  
		  //then call show list view
		  showList();
		  
	}
	public ArrayList<InstagramPhoto> getPhotos() {
		return mPhotos;
	}

	public void setPhotos(ArrayList<InstagramPhoto> mPhotos) {
		this.mPhotos = mPhotos;
	}
	
	public boolean isOnline() {
	    /*ConnectivityManager connectivityManager = (ConnectivityManager) 
	            getSystemService(MainActivity.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	    */
		return true;
	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void Set_Image(String url) {
		img_view=(ImageView)findViewById(R.id.imageView_current);
		img_view.setTag(url);
		new DownloadImageTask().execute(img_view);
	}
	public void showList(){
		PhotoListFragment photoListFragment = new PhotoListFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.linearLayout_result, photoListFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
	public void showGrid(){
		PhotoListFragment photoListFragment = new PhotoListFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.linearLayout_result, photoListFragment);
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
			}else
			if (tab.equals(mGridTab)){
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
				onGetJSON_Finish();
			}else{
				Toast.makeText(MainActivity.this.getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
			}
			mProgressBar.setVisibility(View.GONE);
		}
		
		
		public final static String API_TOKEN = "1124191323.1fb234f.b57465c65f6647af97bf03629f1a4b37";
		public final static String API_PATH = "https://api.instagram.com/v1/media/popular";
		@Override
		protected Long doInBackground(String... params) {
			HttpURLConnection connection = null;
			try {
				URL dataUrl = new URL(API_PATH+"?access_token="+API_TOKEN);
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
				  mPhotos = InstagramPhoto.Load_From_RAW(photoData);
				  
				  //Log.d("connection", photoData);
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
			} finally {
				connection.disconnect();
			}
		}
	}
	public class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {

	    ImageView imageView = null;

	    @Override
	    protected Bitmap doInBackground(ImageView... imageViews) {
	        this.imageView = imageViews[0];
	        return download_Image((String)imageView.getTag());
	    }

	    @Override
	    protected void onPostExecute(Bitmap result) {
	        imageView.setImageBitmap(result);
	    }

	    private Bitmap download_Image(String url) {

	        Bitmap bmp =null;
	        try{
	            URL ulrn = new URL(url);
	            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
	            InputStream is = con.getInputStream();
	            bmp = BitmapFactory.decodeStream(is);
	            if (null != bmp)
	                return bmp;

	            }catch(Exception e){}
	        return bmp;
	    }
	}
}
