package com.qdcatplayer.main.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs._MyDAOAbstract;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.libraries.MyNumberHelper;

import android.media.MediaMetadataRetriever;
import android.text.format.Time;

/**
 * Dinh nghia luon lop My...DAO tuong ung lam viec truc tiep voi class MySong
 * @author quocdunginfo
 *
 */
@DatabaseTable(tableName="MySongs")
public class MySong extends _MyEntityAbstract<MySongDAO> {
	/**
	 * Because 1 song only has 1 title, and common music player
	 * doesn't group songs by title too, no need to create Object
	 * String better
	 */
	@DatabaseField
	private String _title = null;
	
	@DatabaseField
	private Long _duration = null;//milisec
	
	//foreign
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh=true)
	private MyPath _path = null;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private MyFormat _format = null;
	
	@DatabaseField(canBeNull = true, foreign = true)
	private MyAlbum _album = null;
	
	@DatabaseField(canBeNull = true, foreign = true)
	private MyBitrate _bitrate = null;
	
	@DatabaseField(canBeNull = true, foreign = true)
	private MyArtist _artist = null;
	
	public MySong() {
	}
	public MySong(String absPath) {
		//init _path but not load right now
		_path = new MyPath();
		_path.setAbsPath(absPath);
	}
	public Boolean setPath(String absPath) {
		//init new _path
		_path = new MyPath();
		_path.setAbsPath(absPath);
		//then reset
		reset();
		return true;
	}
	public Boolean setPath(MyPath absPath) {
		//init new _path
		_path = absPath;
		//then reset
		reset();
		return true;
	}
	
	public MyPath getPath()
	{
		//pass Global DAO to MyPath for lazy load and after-query if needed
		if(_path!=null)
		{
			_path.setDao(getGlobalDAO().getMyPathDAO());
		}
		return _path;
	}
	public MyFormat getFormat()
	{
		//lazy load
		if(_format!=null)
		{
			return _format;
		}
		//required
		if(getPath()==null)
		{
			return null;
		}
		
		//read sound tag from _path object
		_format = new MyFormat(getPath().getFileExtension(false));
		return _format;
	}
	public MyAlbum getAlbum()
	{
		//lazy load
		if(_album!=null)
		{
			return _album;
		}
		//required
		if(getPath()==null)
		{
			return null;
		}
		
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		_album = new MyAlbum(
				tmp
			);
		return _album;
	}
	public MyArtist getArtist()
	{
		//lazy load
		if(_artist!=null)
		{
			return _artist;
		}
		//required
		if(getPath()==null)
		{
			return null;
		}
		
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		_artist = new MyArtist(
				tmp
			);
		return _artist;
	}
	public MyBitrate getBirate()
	{
		//lazy load
		if(_bitrate!=null)
		{
			return _bitrate;
		}
		//required
		if(getPath()==null)
		{
			return null;
		}
		
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = "128";//retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
		_bitrate = new MyBitrate(
				tmp
			);
		return _bitrate;
	}
	public Time getDuration()
	{
		//lazy load
		if(_duration!=null)
		{
			return MyNumberHelper.toTime(_duration);
		}
		//required
		if(getPath()==null)
		{
			return null;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		return MyNumberHelper.toTime(tmp);
	}
	public String getTitle()
	{
		//lazy load
		if(_title!=null)
		{
			return _title;
		}
		//required
		if(getPath()==null)
		{
			return null;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		_title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		if(_title==null)
		{
			_title = "";
		}
		return _title;
		
	}
	@Override
	public Boolean reset()
	{
		_title=null;
		_album=null;
		_bitrate=null;
		_duration=null;
		_format=null;
		//reset path
		if(_path!=null)
		{
			_path.reset();
		}
		return true;
	}
	@Override
	public Boolean loadAllProperties()
	{
		getAlbum();
		getArtist();
		getBirate();
		getDuration();
		getFormat();
		getId();
		getPath();
		getTitle();
		
		return true;
	}
	@Override
	public Integer insert() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean update() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer delete() {
		// TODO Auto-generated method stub
		return null;
	}
}
