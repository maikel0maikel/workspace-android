package com.example.hour15app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;

public class InstagramContentProvider extends ContentProvider {
	private InstagramPhotoDbAdapter mPhotoDbHelper;

	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		  sUriMatcher.addURI("com.example.hour15app.provider", "instagramphoto", 1);
		  sUriMatcher.addURI("com.example.hour15app.provider", "instagramphoto/#", 2);
		  sUriMatcher.addURI("com.example.hour15app.provider", "instagramphoto/favourite/#", 3);//the last is limit row
		  sUriMatcher.addURI("com.example.hour15app.provider", "instagramphoto/favourite", 4);//
	}
	public static final Uri CONTENT_URI =
			Uri.parse("content://com.example.hour15app.provider/instagramphoto");

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
			return ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.example.hour15app.instagramphoto";
		case 2:
			return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.example.hour15app.instagramphoto";
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
		int  result =  mPhotoDbHelper.mDb.update(InstagramPhotoDbAdapter.DATABASE_TABLE, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return result;


	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int  result =   mPhotoDbHelper.mDb.delete(InstagramPhotoDbAdapter.DATABASE_TABLE,  selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri,null);
		return result;
	}

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		File root = new File(getContext().getApplicationContext().getCacheDir(),  uri.getEncodedPath());
		root.mkdirs();
		File imageFile = new File(root,"image.jpg");
		final OutputStream imageOS;
		final int imode = ParcelFileDescriptor.MODE_READ_ONLY;
		if (imageFile.exists()) {
			return ParcelFileDescriptor.open(imageFile, imode);
		}
		Cursor photoCursor = query(uri, null, null, null, null);
		if (photoCursor == null) return null;
		if (photoCursor.getCount()==0) return null;
		InstagramPhoto currentPhoto = InstagramPhotoDbAdapter.getPhotoFromCursor(photoCursor);
		final String imageString = currentPhoto.img_thumb_url;
		imageOS = new BufferedOutputStream(new FileOutputStream(imageFile));
		RetrieveImage ri = new RetrieveImage (uri, imageString, imageOS);
		ri.execute();
		throw ( new FileNotFoundException());
	}

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



}
