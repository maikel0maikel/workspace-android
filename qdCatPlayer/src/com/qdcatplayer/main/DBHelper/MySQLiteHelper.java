package com.qdcatplayer.main.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MyBitrate;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MyFormat;
import com.qdcatplayer.main.Entities.MyPath;
import com.qdcatplayer.main.Entities.MyPlayList;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.Entities.MySong_MyPlayList;

public class MySQLiteHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "QDCATPLAYER_DB";
	private static final int DATABASE_VERSION = 4;

	public MySQLiteHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void resetDB() {
		onUpgrade(getWritableDatabase(), getConnectionSource(),
				DATABASE_VERSION, DATABASE_VERSION + 1);
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
			TableUtils.createTable(connectionSource, MyPlayList.class);
			TableUtils.createTable(connectionSource, MySong_MyPlayList.class);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		// init new data...
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		// destroy whole db
		try {
			TableUtils.dropTable(connectionSource, MyArtist.class, true);
			TableUtils.dropTable(connectionSource, MyAlbum.class, true);
			TableUtils.dropTable(connectionSource, MyBitrate.class, true);
			TableUtils.dropTable(connectionSource, MyFolder.class, true);
			TableUtils.dropTable(connectionSource, MyFormat.class, true);
			TableUtils.dropTable(connectionSource, MyPath.class, true);
			TableUtils.dropTable(connectionSource, MySong.class, true);
			TableUtils.dropTable(connectionSource, MyPlayList.class, true);
			TableUtils.dropTable(connectionSource, MySong_MyPlayList.class,
					true);

		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// create new one
		onCreate(database, connectionSource);
		// init pre-data
		// ...
	}

	/**
	 * Nen dung RuntimeExceptionDAO vi neu dung Dao binh thuong Trong nhieu
	 * truong hop se bi quang SQLExeption, mac du du lieu van duoc thuc thi tot
	 * !!!!!!!!!
	 */
	private RuntimeExceptionDao<MyAlbum, Integer> _myAlbumDAO = null;

	public RuntimeExceptionDao<MyAlbum, Integer> getMyAlbumDAO() {
		if (_myAlbumDAO == null) {
			_myAlbumDAO = getRuntimeExceptionDao(MyAlbum.class);// do not use
																// DaoManager.create(...);
		}
		return _myAlbumDAO;
	}

	private RuntimeExceptionDao<MySong, Integer> _mySongDAO = null;

	public RuntimeExceptionDao<MySong, Integer> getMySongDAO() {
		if (_mySongDAO == null) {
			_mySongDAO = getRuntimeExceptionDao(MySong.class);// do not use
																// DaoManager.create(...);
		}
		return _mySongDAO;
	}

	private RuntimeExceptionDao<MyArtist, Integer> _myArtistDAO = null;

	public RuntimeExceptionDao<MyArtist, Integer> getMyArtistDAO() {
		if (_myArtistDAO == null) {
			_myArtistDAO = getRuntimeExceptionDao(MyArtist.class);// do not use
																	// DaoManager.create(...);
		}
		return _myArtistDAO;
	}

	private RuntimeExceptionDao<MyBitrate, Integer> _myBitrateDAO = null;

	public RuntimeExceptionDao<MyBitrate, Integer> getMyBitrateDAO() {
		if (_myBitrateDAO == null) {
			_myBitrateDAO = getRuntimeExceptionDao(MyBitrate.class);// do not
																	// use
																	// DaoManager.create(...);
		}
		return _myBitrateDAO;
	}

	private RuntimeExceptionDao<MyFormat, Integer> _myFormatDAO = null;

	public RuntimeExceptionDao<MyFormat, Integer> getMyFormatDAO() {
		if (_myFormatDAO == null) {
			_myFormatDAO = getRuntimeExceptionDao(MyFormat.class);// do not use
																	// DaoManager.create(...);
		}
		return _myFormatDAO;
	}

	private RuntimeExceptionDao<MyFolder, Integer> _myFolderDAO = null;

	public RuntimeExceptionDao<MyFolder, Integer> getMyFolderDAO() {
		if (_myFolderDAO == null) {
			_myFolderDAO = getRuntimeExceptionDao(MyFolder.class);// do not use
																	// DaoManager.create(...);
		}
		return _myFolderDAO;
	}

	private RuntimeExceptionDao<MyPath, Integer> _myPathDAO = null;

	public RuntimeExceptionDao<MyPath, Integer> getMyPathDAO() {
		if (_myPathDAO == null) {
			_myPathDAO = getRuntimeExceptionDao(MyPath.class);// do not use
																// DaoManager.create(...);
		}
		return _myPathDAO;
	}

	private RuntimeExceptionDao<MyPlayList, Integer> _myPlayListDAO = null;

	public RuntimeExceptionDao<MyPlayList, Integer> getMyPlayListDAO() {
		if (_myPlayListDAO == null) {
			_myPlayListDAO = getRuntimeExceptionDao(MyPlayList.class);// do not
																		// use
																		// DaoManager.create(...);
		}
		return _myPlayListDAO;
	}

	private RuntimeExceptionDao<MySong_MyPlayList, Integer> _mySong_MyPlayListDAO = null;

	public RuntimeExceptionDao<MySong_MyPlayList, Integer> getMySong_MyPlayListDAO() {
		if (_mySong_MyPlayListDAO == null) {
			_mySong_MyPlayListDAO = getRuntimeExceptionDao(MySong_MyPlayList.class);// do
																					// not
																					// use
																					// DaoManager.create(...);
		}
		return _mySong_MyPlayListDAO;
	}

	@Override
	public void close() {
		// very importance because àter close there are no way to get it open
		// again
		// not tested all yet but at current time, do not call super.close or
		// you
		// might be encountred illegalExcaption when reset DB via upgrade
		// super.close();
		_myArtistDAO = null;
		_myBitrateDAO = null;
		_myFolderDAO = null;
		_myFormatDAO = null;
		_myPathDAO = null;
		_mySongDAO = null;
		_myAlbumDAO = null;
		_myPlayListDAO = null;
		_mySong_MyPlayListDAO = null;
	}
}
