package com.qdcatplayer.main.DBHelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
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
	private static final int DATABASE_VERSION = 4;
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
	/**
	 * Nen dung RuntimeExceptionDAO vi neu dung Dao binh thuong
	 * Trong nhieu truong hop se bi quang SQLExeption, mac du du lieu van duoc
	 * thuc thi tot !!!!!!!!!
	 */
	private RuntimeExceptionDao<MyAlbum, Integer> _myAlbumDAO = null;
	public RuntimeExceptionDao<MyAlbum, Integer> getMyAlbumDAO()
	{
		if(_myAlbumDAO==null)
		{
			_myAlbumDAO = getRuntimeExceptionDao(MyAlbum.class);//do not use DaoManager.create(...);
		}
		return _myAlbumDAO;
	}
	private RuntimeExceptionDao<MySong, Integer> _mySongDAO = null;
	public RuntimeExceptionDao<MySong, Integer> getMySongDAO()
	{
		if(_mySongDAO==null)
		{
			_mySongDAO = getRuntimeExceptionDao(MySong.class);//do not use DaoManager.create(...);
		}
		return _mySongDAO;
	}
	private RuntimeExceptionDao<MyArtist, Integer> _myArtistDAO = null;
	public RuntimeExceptionDao<MyArtist, Integer> getMyArtistDAO()
	{
		if(_myArtistDAO==null)
		{
			_myArtistDAO = getRuntimeExceptionDao(MyArtist.class);//do not use DaoManager.create(...);
		}
		return _myArtistDAO;
	}
	private RuntimeExceptionDao<MyBitrate, Integer> _myBitrateDAO = null;
	public RuntimeExceptionDao<MyBitrate, Integer> getMyBitrateDAO()
	{
		if(_myBitrateDAO==null)
		{
			_myBitrateDAO = getRuntimeExceptionDao(MyBitrate.class);//do not use DaoManager.create(...);
		}
		return _myBitrateDAO;
	}
	private RuntimeExceptionDao<MyFormat, Integer> _myFormatDAO = null;
	public RuntimeExceptionDao<MyFormat, Integer> getMyFormatDAO()
	{
		if(_myFormatDAO==null)
		{
			_myFormatDAO = getRuntimeExceptionDao(MyFormat.class);//do not use DaoManager.create(...);
		}
		return _myFormatDAO;
	}
	private RuntimeExceptionDao<MyFolder, Integer> _myFolderDAO = null;
	public RuntimeExceptionDao<MyFolder, Integer> getMyFolderDAO()
	{
		if(_myFolderDAO==null)
		{
			_myFolderDAO = getRuntimeExceptionDao(MyFolder.class);//do not use DaoManager.create(...);
		}
		return _myFolderDAO;
	}
	private RuntimeExceptionDao<MyPath, Integer> _myPathDAO = null;
	public RuntimeExceptionDao<MyPath, Integer> getMyPathDAO()
	{
		if(_myPathDAO==null)
		{
			_myPathDAO = getRuntimeExceptionDao(MyPath.class);//do not use DaoManager.create(...);
		}
		return _myPathDAO;
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
