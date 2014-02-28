package com.bffmedia.hour15app;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class FlickrPhotoDbAdapter {


	public static final String KEY_ROWID = "_id";
	public static final String[] FLICKR_PHOTO_FIELDS = new String[] {
		KEY_ROWID,    
		"flickr_id", // flickr id of the photo
		"owner",
		"secret", 
		"server",
		"farm",	
		"title",
		"isPublic",
		"isFriend",
		"isFamily",
		"isFavorite"
	};


	private DatabaseHelper mDbHelper;
	SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 * Modeled on notes app
	 * id integer is primary key
	 */
	private static final String DATABASE_CREATE =
		"create table flickrphoto (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ "flickr_id not null,"
		+ "owner text,"
		+ "secret text,"		
		+ "server text,"
		+ "farm text,"
		+ "title text,"
		+ "isPublic INTEGER,"
		+ "isFriend INTEGER,"	
		+ "isFamily INTEGER,"	
		+ "isFavorite INTEGER"	
		+");";

	

	
	
	private final Context mCtx;
	public static String TAG = FlickrPhotoDbAdapter.class.getSimpleName();
	 static final String DATABASE_NAME = "FLICKR_PHOTOS";
	 static final String DATABASE_TABLE = "flickrphoto";
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
			db.execSQL("DROP TABLE IF EXISTS flickrphoto");
			onCreate(db);
		}
	}


	public FlickrPhotoDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public FlickrPhotoDbAdapter open() throws SQLException {
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



	public long createPhoto(FlickrPhoto photoToCreate) {
		ContentValues initialValues = new ContentValues();
		if (photoToCreate.id!=null)
			initialValues.put("flickr_id", photoToCreate.id);	
		if (photoToCreate.owner!=null)
			initialValues.put("owner", photoToCreate.owner);	
		if (photoToCreate.secret!=null)
			initialValues.put("secret", photoToCreate.secret);	
		if (photoToCreate.server!=null)
			initialValues.put("server", photoToCreate.server);	
		if (photoToCreate.farm!=null)
			initialValues.put("farm", photoToCreate.farm);	
		if (photoToCreate.title!=null)
			initialValues.put("title", photoToCreate.title);	
		if (photoToCreate.isPublic!=null)
			initialValues.put("isPublic", photoToCreate.isPublic);	
		if (photoToCreate.isFriend!=null)
			initialValues.put("isFriend", photoToCreate.isFriend);	
		if (photoToCreate.isFamily!=null)
			initialValues.put("isFamily", photoToCreate.isFamily);	
		if (photoToCreate.isFavorite!=null)
			initialValues.put("isFavorite", photoToCreate.isFavorite);		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}


	public boolean deletePhoto(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean deletePhoto(String photo_id) {
		return mDb.delete(DATABASE_TABLE, "flickr_id" + "=" + photo_id, null) > 0;
	}


	public Cursor fetchPhotos() {

		return mDb.query(DATABASE_TABLE, FLICKR_PHOTO_FIELDS, null, null, null, null, null);
	}


	public Cursor fetchById(long rowId) throws SQLException {
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, FLICKR_PHOTO_FIELDS, KEY_ROWID + "=" + rowId, null,
					null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor fetchByFlickrId(String flickrId) throws SQLException {
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, FLICKR_PHOTO_FIELDS, "flickr_id" + "='" + flickrId+"'", null,
					null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetchFavorites() throws SQLException {
		Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, FLICKR_PHOTO_FIELDS, "isFavorite=1", null,
					null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	
	public FlickrPhoto getPhotoByFlickrId(String id){
		FlickrPhoto photo = null;
		Cursor photoCursor = fetchByFlickrId(id);
		if (photoCursor.moveToFirst()){
			photo= getPhotoFromCursor(photoCursor);
		}
		if (photoCursor!=null){
			photoCursor.close();
		}
		return photo;
	}


	public static FlickrPhoto getPhotoFromCursor(Cursor cursor){
		FlickrPhoto photo = new FlickrPhoto();
		photo.id = cursor.getString(cursor.getColumnIndex("flickr_id"));
		photo.owner = cursor.getString(cursor.getColumnIndex("owner"));
		photo.secret = cursor.getString(cursor.getColumnIndex("secret"));
		photo.server = cursor.getString(cursor.getColumnIndex("server"));
		photo.farm = cursor.getString(cursor.getColumnIndex("farm"));
		photo.title = cursor.getString(cursor.getColumnIndex("title"));
		photo.isPublic = (cursor.getInt(cursor.getColumnIndex("isPublic")) == 1);
		photo.isFriend = (cursor.getInt(cursor.getColumnIndex("isFriend")) == 1);
		photo.isFamily = (cursor.getInt(cursor.getColumnIndex("isFamily")) == 1);
		photo.isFavorite = (cursor.getInt(cursor.getColumnIndex("isFavorite")) == 1);
		return(photo);
	}


	public boolean updatePhoto(String id, FlickrPhoto photoToUpdate) {
		ContentValues updateValues = new ContentValues();
		if (photoToUpdate.id!=null)
			updateValues.put("flickr_id", photoToUpdate.id);	
		if (photoToUpdate.owner!=null)
			updateValues.put("owner", photoToUpdate.owner);	
		if (photoToUpdate.secret!=null)
			updateValues.put("secret", photoToUpdate.secret);	
		if (photoToUpdate.server!=null)
			updateValues.put("server", photoToUpdate.server);	
		if (photoToUpdate.farm!=null)
			updateValues.put("farm", photoToUpdate.farm);	
		if (photoToUpdate.title!=null)
			updateValues.put("title", photoToUpdate.title);	
		if (photoToUpdate.isPublic!=null)
			updateValues.put("isPublic", photoToUpdate.isPublic);	
		if (photoToUpdate.isFriend!=null)
			updateValues.put("isFriend", photoToUpdate.isFriend);	
		if (photoToUpdate.isFamily!=null)
			updateValues.put("isFamily", photoToUpdate.isFamily);	
		if (photoToUpdate.isFavorite!=null)
			updateValues.put("isFavorite", photoToUpdate.isFavorite);	
		return mDb.update(DATABASE_TABLE, updateValues, "flickr_id" + "=" + id, null) > 0;
	}


}


//
////set the format to sql date time
//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//Date date = new Date();
//ContentValues initialValues = new ContentValues(); 
//initialValues.put("date_created", dateFormat.format(date));
//long rowId = mDb.insert(DATABASE_TABLE, null, initialValues);