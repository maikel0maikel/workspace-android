package com.example.hour15app;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class InstagramPhotoDbAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String[] INSTAGRAM_PHOTO_FIELDS = new String[] {
		KEY_ROWID,    
		"instagram_id", // instagram id of the photo
		"owner",
		"title", 
		"img_thumb_url",
		"is_favorite"
	};


	private DatabaseHelper mDbHelper;
	SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 * Modeled on notes app
	 * id integer is primary key
	 */
	private static final String DATABASE_CREATE =
		"create table instagramphoto (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "instagram_id not null,"
		+ "owner text,"
		+ "img_thumb_url text,"
		+ "title text,"		
		+ "is_favorite INTEGER"	
		+");";

	private final Context mCtx;
	public static String TAG = InstagramPhotoDbAdapter.class.getSimpleName();
	private static final String DATABASE_NAME = "INSTAGRAM_PHOTOS";
	public static final String DATABASE_TABLE = "instagramphoto";
	private static final int DATABASE_VERSION = 1;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG , "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS ckrphoto");
			onCreate(db);
		}
	}


	public InstagramPhotoDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public InstagramPhotoDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	public void close() {
		if(mDbHelper!=null){
			mDbHelper.close();
		}
	}
	public void upgrade() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx); //open
		mDb = mDbHelper.getWritableDatabase();
		mDbHelper.onUpgrade(mDb, 1, 0);
	}



	public long createPhoto(InstagramPhoto photoToCreate) {
		ContentValues initialValues = new ContentValues();
		if (photoToCreate.id!=null)
			initialValues.put("instagram_id", photoToCreate.id);	
		if (photoToCreate.title!=null)
			initialValues.put("title", photoToCreate.title);
		if (photoToCreate.owner!=null)
			initialValues.put("owner", photoToCreate.owner);	
		if (photoToCreate.img_thumb_url!=null)
			initialValues.put("img_thumb_url", photoToCreate.img_thumb_url);	
		if (photoToCreate.is_favourite!=null)
			initialValues.put("is_favorite", photoToCreate.is_favourite);	
				
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deletePhoto(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean deletePhoto(String photo_id) {
		return mDb.delete(DATABASE_TABLE, "instagram_id" + "=" + photo_id, null) > 0;
	}


	public Cursor fetchPhotos() {
		return mDb.query(DATABASE_TABLE, INSTAGRAM_PHOTO_FIELDS, null, null, null, null, null);
	}


	public Cursor fetchById(long rowId) throws SQLException {
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, INSTAGRAM_PHOTO_FIELDS, KEY_ROWID + "=" + rowId, null,
					null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor fetchByInstagramId(String instagramId) throws SQLException {
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, INSTAGRAM_PHOTO_FIELDS, "instagram_id" + "='" + instagramId+"'", null,
					null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetchFavorites() throws SQLException {
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, INSTAGRAM_PHOTO_FIELDS, "is_favorite=1", null,
					null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	
	public InstagramPhoto getPhotoByInstagramId(String id){
		InstagramPhoto photo = null;
		Cursor photoCursor = fetchByInstagramId(id);
		if (photoCursor.moveToFirst()){
			photo= getPhotoFromCursor(photoCursor);
		}
		if (photoCursor!=null){
			//photoCursor.close();
		}
		return photo;
	}


	public static InstagramPhoto getPhotoFromCursor(Cursor cursor){
		InstagramPhoto photo = new InstagramPhoto();
		//photo.id = cursor.getString(cursor.getColumnIndex("instagram_id"));
		photo.owner = cursor.getString(cursor.getColumnIndex("owner"));
		photo.title = cursor.getString(cursor.getColumnIndex("title"));
		photo.img_thumb_url = cursor.getString(cursor.getColumnIndex("img_thumb_url"));
		photo.is_favourite = cursor.getInt(cursor.getColumnIndex("is_favorite"));
		return(photo);
	}

	public boolean updatePhoto(String id, InstagramPhoto photoToUpdate) {
		ContentValues updateValues = new ContentValues();
		if (photoToUpdate.owner!=null)
			updateValues.put("owner", photoToUpdate.owner);
		if (photoToUpdate.title!=null)
			updateValues.put("title", photoToUpdate.title);
		if (photoToUpdate.img_thumb_url!=null)
			updateValues.put("img_thumb_url", photoToUpdate.img_thumb_url);	
		if (photoToUpdate.is_favourite!=null)
			updateValues.put("is_favorite", photoToUpdate.is_favourite);	
		return mDb.update(DATABASE_TABLE, updateValues, "instagram_id" + "='" + id+"'", null) > 0;//quen dau ' la chet nguoi, cu bao conflict suot
	}
}