package com.example.hour15app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import com.example.hour15app.InstagramPhoto;
import com.example.hour15app.R;


import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	ProgressBar mProgressBar;
	Tab mListTab;
	Tab mGridTab;
	ImageView img_view;
	public static int limit_r=20;
	private ArrayList<InstagramPhoto> mPhotos = new ArrayList<InstagramPhoto>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layt);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar_working);
		
		if (isOnline()){
			LoadPhotos task = new LoadPhotos();
			task.execute();
		}else{
			mProgressBar.setVisibility(View.GONE);
			Toast.makeText(MainActivity.this.getApplicationContext(), "Please connect to retrieve photos", Toast.LENGTH_SHORT).show();
		}
		//get setting
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		limit_r = sharedPref.getInt("limit_rows", 20);
		
	}
	//call by Asyntask when finish
	private void onGetJSON_Finish()
	{
		InstagramPhotoDbAdapter mDB = new InstagramPhotoDbAdapter(MainActivity.this);
		mDB.open();
		int ii=0;
		  for(InstagramPhoto item:mPhotos)
		  {
			  if(ii%2==0) item.is_favourite=1;//random favourite
			  ii++;
			  //nếu title||img == rỗng thì b�? qua
			  if(item.img_thumb_url.equals("") || item.title.equals("")) continue;
			  
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
			  }
			  
		  }
		  mDB.close();
		  
		  //
		  //then call show list view
		  showList();
		  
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            //display fragment activity
	        	Intent intt = new Intent(MainActivity.this, QdSettingActivity.class);
	        	startActivity(intt);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void Set_Image(String url) {
		/*
		img_view=(ImageView)findViewById(R.id.imageView_current);
		img_view.setTag(url);
		new DownloadImageTask().execute(img_view);
		*/
	}
	private void Show_Load_IMG_Status(Boolean load_from_cache)
	{
		if(load_from_cache)
		{
			Toast.makeText(this, "Load IMG from Cache dir", 2000).show();
		}
		else
		{
			Toast.makeText(this, "Get IMG from Internet and write to Cache for next request", 2000).show();
		}
	}
	public void showList(){
		
		Log.w("qd", "PhotoGalleryFragment called for replacing 'linearLayout_result' holder");
		PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.linearLayout_result, photoGalleryFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		ft.commit();
		mProgressBar.setVisibility(ProgressBar.INVISIBLE);
		
		/*
		PhotoListFragment photoGalleryFragment = new PhotoListFragment();
		android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.linearLayout_result, photoGalleryFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		WORK OK............ */
	}
	public void showGrid(){
		/*
		PhotoGridFragment photoListFragment = new PhotoGridFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.linearLayout_result, photoListFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		*/
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
		}
		
		
		public final static String API_TOKEN = "1124191323.1fb234f.b57465c65f6647af97bf03629f1a4b37";
		public final static String API_PATH = "https://api.instagram.com/v1/media/popular";
		@Override
		protected Long doInBackground(String... params) {
			HttpURLConnection connection = null;
			try {
				Log.w("qd", "Open Internet connection for downloading JSON...");
				
				URL dataUrl = new URL(API_PATH+"?access_token="+API_TOKEN);
				//dataUrl = new URL("http://quocdung.net/test/json.html");
				
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
				  Log.w("qd", "Get JSON raw data OK, API_PATH="+dataUrl.getPath());
				  
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
		
		private Boolean load_from_cache=false;
	    ImageView imageView = null;
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		Log.w("qd", "AsyncTask DownloadImageTask has been started...");
    	}
	    
	    @Override
	    protected Bitmap doInBackground(ImageView... imageViews) {
	        this.imageView = imageViews[0];
	        return download_Image((String)imageView.getTag());//get url that set before
	    }

	    @Override
	    protected void onPostExecute(Bitmap result) {
	    	imageView.setImageBitmap(result);
	    	Log.w("qd", "Set IMG to ImageView in main layout OK, ViewID="+imageView.getId());
	    	//show status
	    	Show_Load_IMG_Status(load_from_cache);
	    }

	    private Bitmap download_Image(String url) {//checked, work ok
	    	//ghi hình ảnh vào bộ nhớ tạm
	    	String url_t = (String)imageView.getTag();
	    	String md5_h = MD5_Fnc.md5(url_t);
	    	//kiểm tra thư mục thumbs có được tạo lần đầu chưa
	    	File root = new File(MainActivity.this.getCacheDir(), "thumbs");
	    	if(!root.exists())
	    	{
	    		Log.w("qd", "[Cache dir]/thumbs/ created, abs_filename="+root.getAbsolutePath());
	    		root.mkdir();
	    	}
	    	//
	    	File imageFile = new File(root, md5_h+".jpg");
	    	Bitmap bmp =null;
	    	if (imageFile.exists ())//nếu đã có file rồi
	    	{
	    		Log.w("qd", "Img already existed inCache dir, abs_filename="+imageFile.getAbsolutePath());
	    		//đ�?c file lên bmp
	    		bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
	    		Log.w("qd", "Read img from Cache dir successfully, abs_filename="+imageFile.getAbsolutePath());
	    		load_from_cache=true;
	    	}
	    	else//nếu chưa có file
	    	{
		    	//get img from internet
		        try{
		            URL ulrn = new URL(url);
		            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
		            InputStream is = con.getInputStream();
		            bmp = BitmapFactory.decodeStream(is);
		            Log.w("qd", "Get img from Internet successfully, url="+url);
		            load_from_cache=false;
		            
	            }catch(Exception e){
	            	bmp=null;
	            }
		        
		        //sau đó ghi xuống bộ nhớ tạm
		        if(bmp!=null)
		        {
		    		FileOutputStream out;
					try {
						out = new FileOutputStream(imageFile);
						bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
				    	out.flush();
				    	out.close();
				    	Log.w("qd", "Write img to Cache dir successfully, abs_path="+imageFile.getAbsolutePath());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
	    	}
	    	
	        return bmp;
	    }
	}
}
