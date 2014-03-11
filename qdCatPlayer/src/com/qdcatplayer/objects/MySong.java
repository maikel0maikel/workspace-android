package com.qdcatplayer.objects;

import com.qdcatplayer.libraries.MyNumberHelper;

import android.media.MediaMetadataRetriever;
import android.text.format.Time;

public class MySong {
	private String _title = null;
	private Time _duration = null;
	
	//foreign
	private MyPath _path = null;
	private MyFormat _format = null;
	private MyAlbum _album = null;
	private MyBitrate _bitrate = null;
	public MySong() {
		// TODO Auto-generated constructor stub
		
	}
	public MySong(String absPath) {
		// TODO Auto-generated constructor stub
		//init _path but not load right now
		_path = new MyPath();
		_path.setAbsPath(absPath);
	}
	
	public MyPath getPath()
	{
		return _path;
	}
	public MyFormat getFormat()
	{
		//required
		if(getPath()==null)
		{
			return null;
		}
		//lazy load
		if(_format!=null)
		{
			return _format;
		}
		//read sound tag from _path object
		_format = new MyFormat(getPath().getFileExtension());
		return _format;
	}
	public MyAlbum getAlbum()
	{
		//required
		if(getPath()==null)
		{
			return null;
		}
		//lazy load
		if(_album!=null)
		{
			return _album;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		_album = new MyAlbum(
				tmp
			);
		return _album;
	}
	public MyBitrate getBirate()
	{
		//required
		if(getPath()==null)
		{
			return null;
		}
		//lazy load
		if(_bitrate!=null)
		{
			return _bitrate;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
		_bitrate = new MyBitrate(
				tmp
			);
		return _bitrate;
	}
	public Time getDuration()
	{
		//required
		if(getPath()==null)
		{
			return null;
		}
		//lazy load
		if(_duration!=null)
		{
			return _duration;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		_duration = MyNumberHelper.toTime(tmp);
		return _duration;
	}
	public String getTitle()
	{
		//required
		if(getPath()==null)
		{
			return null;
		}
		//lazy load
		if(_title!=null)
		{
			return _title;
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
}
