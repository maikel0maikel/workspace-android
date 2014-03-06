package com.example.hour16app2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

public class InstagramContentProvider extends ContentProvider {
	private InstagramPhotoDbAdapter mPhotoDbHelper;

	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		  sUriMatcher.addURI("com.example.hour16app2.provider", "instagramphoto", 1);
		  sUriMatcher.addURI("com.example.hour16app2.provider", "instagramphoto/*", 2);
		  sUriMatcher.addURI("com.example.hour16app2.provider", "instagramphoto/favourite/*", 3);//the last is limit row
		  sUriMatcher.addURI("com.example.hour16app2.provider", "instagramphoto/favourite", 4);//
	}
	public static final Uri CONTENT_URI =
			Uri.parse("content://com.example.hour16app2.provider/instagramphoto");

	@Override
	public boolean onCreate() {
		mPhotoDbHelper = new InstagramPhotoDbAdapter(getContext());
		mPhotoDbHelper.open();
		return true;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor;
		int uriType = sUriMatcher.match(uri);
		switch (uriType) {
		case 1:
				cursor = mPhotoDbHelper.mDb.query(true, InstagramPhotoDbAdapter.DATABASE_TABLE, 
					projection, selection, selectionArgs, null,null,sortOrder, null);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			break;
		case 2:
			String tmp = uri.getLastPathSegment();
			cursor =  mPhotoDbHelper.fetchByInstagramId(uri.getLastPathSegment());
			cursor.setNotificationUri(getContext().getContentResolver(), uri);			
			break;
		case 3:
			int limit_r = 20;//by default
			if(!uri.getLastPathSegment().equals(""))
			{
				try {
					limit_r = Integer.parseInt(uri.getLastPathSegment());
				}catch(Exception ex){limit_r=20;}
			}
			cursor =  mPhotoDbHelper.mDb.query(true, InstagramPhotoDbAdapter.DATABASE_TABLE,
					projection, "is_favorite=1", selectionArgs, null, null,
					"instagram_id desc, title asc", String.valueOf(limit_r));
			cursor.setNotificationUri(getContext().getContentResolver(), uri);			
			break;
		case 4:
			cursor =  mPhotoDbHelper.mDb.query(true, InstagramPhotoDbAdapter.DATABASE_TABLE,
					projection, "is_favorite=1", selectionArgs, null, null,
					"instagram_id desc, title asc", null);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);			
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		return cursor;
	}


	@Override
	public String getType(Uri uri) {
		int uriType = sUriMatcher.match(uri);
		switch (uriType) {
		case 1:
		case 3:
			return ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.example.hour16app2.instagramphoto";
		case 2:
			return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.example.hour16app2.instagramphoto";
		default:
			return null;
		}
	}
	
	@Override
	public String[] getStreamTypes (Uri uri, String mimeTypeFilter){
		return new String[] {"image/jpeg"};
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long newID = mPhotoDbHelper.mDb.insert(InstagramPhotoDbAdapter.DATABASE_TABLE, null, values);
		if (newID > 0) {
			Uri newUri = ContentUris.withAppendedId(uri, newID);
			getContext().getContentResolver().notifyChange(uri, null);
			return newUri;
		} else {
			throw new SQLException("Failed to insert row into " + uri);
		}	
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return mPhotoDbHelper.mDb.update(InstagramPhotoDbAdapter.DATABASE_TABLE, values, selection, selectionArgs);
		//getContext().getContentResolver().notifyChange(uri, null);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int  result =   mPhotoDbHelper.mDb.delete(InstagramPhotoDbAdapter.DATABASE_TABLE,  selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri,null);
		return result;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException{
		Bitmap re=null;
		QdBitmap tmp= new QdBitmap();
		tmp.bitmap=re;
		tmp.mCtx= getContext();
		tmp.mUri= uri;
		
		Cursor photoCursor = query(uri, null, null, null, null);
		if (photoCursor == null) return null;
		if (photoCursor.getCount()==0) return null;
		InstagramPhoto currentPhoto = InstagramPhotoDbAdapter.getPhotoFromCursor(photoCursor);
		String imageString = currentPhoto.img_thumb_url;
		tmp.url = imageString;
		
		//check img có rồi
    	String url_t = (String)tmp.url;
    	String md5_h = MD5_Fnc.md5(url_t);
    	//kiểm tra thư mục thumbs có được tạo lần đầu chưa
    	File root = new File(tmp.mCtx.getCacheDir(), "thumbs");
    	if(root.exists())
    	{
    		//
        	File imageFile = new File(root, md5_h+".jpg");
        	if (imageFile.exists ())//nếu đã có file rồi
        	{
        		try {
					return ParcelFileDescriptor.open(imageFile, ParcelFileDescriptor.MODE_READ_ONLY);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
    	}
    	DownloadImageTask tsk=new DownloadImageTask();
		tsk.execute(tmp);
		//img is still getting....
		throw ( new FileNotFoundException() );
		
		/*
		File root = new File(getContext().getApplicationContext().getCacheDir(),  uri.getEncodedPath());
		root.mkdirs();
		File imageFile = new File(root,"image.jpg");
		final OutputStream imageOS;
		final int imode = ParcelFileDescriptor.MODE_READ_ONLY;
		if (imageFile.exists()) {
			return ParcelFileDescriptor.open(imageFile, imode);
		}
		String hjhjdsf=uri.getLastPathSegment();
		Cursor photoCursor = query(uri, null, null, null, null);
		if (photoCursor == null) return null;
		if (photoCursor.getCount()==0) return null;
		InstagramPhoto currentPhoto = InstagramPhotoDbAdapter.getPhotoFromCursor(photoCursor);
		final String imageString = currentPhoto.img_thumb_url;
		imageOS = new BufferedOutputStream(new FileOutputStream(imageFile));
		RetrieveImage ri = new RetrieveImage (uri, imageString, imageOS);
		ri.execute();
		throw (
				new FileNotFoundException()
			);
			*/
	}
	/*
	private class RetrieveImage extends AsyncTask<String , String , Long > {
		String  mImageString;
		OutputStream mImageOS;
		Uri mUri;
		public RetrieveImage ( Uri uri, String imageString, OutputStream imageOS){
			mImageString = imageString;
			mImageOS = imageOS;
			mUri = uri;
		}	
		@Override
		protected Long doInBackground(String... params) {
			try {
				URL imageUrl = new URL(mImageString);
				URLConnection connection = imageUrl.openConnection();
				connection.connect();
				InputStream is = connection.getInputStream();
				Bitmap mBitmap = BitmapFactory.decodeStream(is);
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, mImageOS);
				mImageOS.flush();
				mImageOS.close();
				getContext().getContentResolver().notifyChange(mUri, null);
				return (0l);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return (1l);
			} 
			catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
				return (1l);
			}
		}
	}
	*/
	
	//quocdunginfo
	public class QdBitmap{
		public Bitmap bitmap;
		public String url;
		public Context mCtx;
		public Uri mUri;
	}
	public class DownloadImageTask extends AsyncTask<QdBitmap, Void, Bitmap> {
		
		private Boolean load_from_cache=false;
	    QdBitmap imageView = null;
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		Log.w("qd", "AsyncTask DownloadImageTask has been started...");
    	}
	    
	    @Override
	    protected Bitmap doInBackground(QdBitmap... imageViews) {
	        this.imageView = imageViews[0];
	        return download_Image((String)imageView.url);//get url that set before
	    }

	    @Override
	    protected void onPostExecute(Bitmap result) {
	    	imageView.bitmap = result;
	    	Log.w("qd", "Load Bitmap OK");
	    	//show status
	    	//Show_Load_IMG_Status(load_from_cache);
	    	getContext().getContentResolver().notifyChange(imageView.mUri, null);
	    }

	    private Bitmap download_Image(String url) {//checked, work ok
	    	//ghi hình ảnh vào bộ nhớ tạm
	    	String url_t = (String)imageView.url;
	    	String md5_h = MD5_Fnc.md5(url_t);
	    	//kiểm tra thư mục thumbs có được tạo lần đầu chưa
	    	File root = new File(imageView.mCtx.getCacheDir(), "thumbs");
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
	    		//đọc file lên bmp
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
