package com.qdcatplayer.main.DBHelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MyArtist;
import com.qdcatplayer.main.entities.MyBitrate;
import com.qdcatplayer.main.entities.MyFolder;
import com.qdcatplayer.main.entities.MyFormat;
import com.qdcatplayer.main.entities.MyPath;
import com.qdcatplayer.main.entities.MySong;

public class MySQLiteHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "QDCATPLAYER_DB";
	private static final int DATABASE_VERSION = 1;
	public MySQLiteHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
            TableUtils.createTable(connectionSource, MyArtist.class);
            TableUtils.createTable(connectionSource, MyAlbum.class);
            TableUtils.createTable(connectionSource, MyBitrate.class);
            TableUtils.createTable(connectionSource, MyFolder.class);
            TableUtils.createTable(connectionSource, MyFormat.class);
            TableUtils.createTable(connectionSource, MyPath.class);
            TableUtils.createTable(connectionSource, MySong.class);
        } catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
        //init new data...
    }

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		//destroy whole db
		try {
			TableUtils.dropTable(connectionSource, MyArtist.class, true);
			TableUtils.dropTable(connectionSource, MyAlbum.class, true);
			TableUtils.dropTable(connectionSource, MyBitrate.class, true);
			TableUtils.dropTable(connectionSource, MyFolder.class, true);
			TableUtils.dropTable(connectionSource, MyFormat.class, true);
			TableUtils.dropTable(connectionSource, MyPath.class, true);
			TableUtils.dropTable(connectionSource, MySong.class, true);
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//create new one
		onCreate(database, connectionSource);
		//init pre-data
		//...
	}
	//original DAOs provider
	private Dao<MySong, Integer> _mySongDAO = null;
	public Dao<MySong, Integer> getMySongDAO()
	{
		if(_mySongDAO!=null)
		{
			return _mySongDAO;
		}
		try {
			_mySongDAO = getDao(MySong.class);//do not use DaoManager.create(...);
			return _mySongDAO;
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private Dao<MyArtist, Integer> _myArtistDAO = null;
	public Dao<MyArtist, Integer> getMyArtistDAO()
	{
		if(_myArtistDAO!=null)
		{
			return _myArtistDAO;
		}
		try {
			_myArtistDAO = getDao(MyArtist.class);//do not use DaoManager.create(...);
			return _myArtistDAO;
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private Dao<MyBitrate, Integer> _myBitrateDAO = null;
	public Dao<MyBitrate, Integer> getMyBitrateDAO()
	{
		if(_myBitrateDAO!=null)
		{
			return _myBitrateDAO;
		}
		try {
			_myBitrateDAO = getDao(MyBitrate.class);//do not use DaoManager.create(...);
			return _myBitrateDAO;
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private Dao<MyFormat, Integer> _myFormatDAO = null;
	public Dao<MyFormat, Integer> getMyFormatDAO()
	{
		if(_myFormatDAO!=null)
		{
			return _myFormatDAO;
		}
		try {
			_myFormatDAO = getDao(MyFormat.class);//do not use DaoManager.create(...);
			return _myFormatDAO;
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private Dao<MyFolder, Integer> _myFolderDAO = null;
	public Dao<MyFolder, Integer> getMyFolderDAO()
	{
		if(_myFolderDAO!=null)
		{
			return _myFolderDAO;
		}
		try {
			_myFolderDAO = getDao(MyFolder.class);//do not use DaoManager.create(...);
			return _myFolderDAO;
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private Dao<MyPath, Integer> _myPathDAO = null;
	public Dao<MyPath, Integer> getMyPathDAO()
	{
		if(_myPathDAO!=null)
		{
			return _myPathDAO;
		}
		try {
			_myPathDAO = getDao(MyPath.class);//do not use DaoManager.create(...);
			return _myPathDAO;
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
		_myArtistDAO=null;
		_myBitrateDAO=null;
		_myFolderDAO=null;
		_myFormatDAO=null;
		_myPathDAO=null;
		_mySongDAO=null;
	}
}
