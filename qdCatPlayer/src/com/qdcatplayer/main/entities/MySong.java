package com.qdcatplayer.main.entities;

import android.media.MediaMetadataRetriever;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.libraries.MyNumberHelper;

/**
 * Dinh nghia luon lop My...DAO tuong ung lam viec truc tiep voi class MySong
 * Neu khai bao 1 Entity thi no van chua hieu phai lam viec voi Disk Hay DB Chi
 * co the dung de pass data giua cac Activity Muon Entity lam viec duoc voi du
 * lieu thi bat buoc phai setDAO(My...DAO) va DAO phai duoc chi dinh SOURCE
 * truoc, thong qua My...DAO.setSource(SOURCE)
 * 
 * @author quocdunginfo
 * 
 */
@DatabaseTable(tableName = "MySongs")
public class MySong extends _MyEntityAbstract<MySongDAO> {
	@DatabaseField(canBeNull = true, foreign = true)
	private MyAlbum album = null;

	@DatabaseField(canBeNull = true, foreign = true)
	private MyArtist artist = null;

	@DatabaseField(canBeNull = true, foreign = true)
	private MyBitrate bitrate = null;

	@DatabaseField(canBeNull = true)
	private Long duration = null;// milisec

	@DatabaseField(canBeNull = false, foreign = true)
	private MyFormat format = null;

	// foreign
	@DatabaseField(canBeNull = false, foreign = true)
	private MyPath path = null;

	/**
	 * Because 1 song only has 1 title, and common music player doesn't group
	 * songs by title too, no need to create Object String better
	 */
	@DatabaseField(useGetSet = true)
	private String title = null;

	public MySong() {
	}

	public MySong(String absPath) {
		// init _path but not load right now
		path = new MyPath();
		path.setAbsPath(absPath);
	}

	@Override
	public Integer delete() {
		// TODO Auto-generated method stub
		return null;
	}

	public MyAlbum getAlbum() {
		// lazy load
		if (album != null) {
			return album;
		}
		// required
		if (getPath() == null) {
			return null;
		}

		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		album = new MyAlbum(tmp);
		return album;
	}

	public MyArtist getArtist() {
		// lazy load
		if (artist != null) {
			return artist;
		}
		// required
		if (getPath() == null) {
			return null;
		}

		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		artist = new MyArtist(tmp);
		return artist;
	}

	public MyBitrate getBirate() {
		// lazy load
		if (bitrate != null) {
			return bitrate;
		}
		// required
		if (getPath() == null) {
			return null;
		}

		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = "128";// retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
		bitrate = new MyBitrate(tmp);
		return bitrate;
	}

	public MyBitrate getBitrate() {
		return bitrate;
	}

	public Long getDuration() {
		// lazy load
		if (duration != null) {
			return duration;
		}
		// required
		if (getPath() == null) {
			return null;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		String tmp = retriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		duration = MyNumberHelper.stringToLong(tmp);
		return duration;
	}

	public MyFormat getFormat() {
		// lazy load
		if (format != null) {
			return format;
		}
		// required
		if (getPath() == null) {
			return null;
		}

		// read sound tag from _path object
		format = new MyFormat(getPath().getFileExtension(false));
		return format;
	}

	public MyPath getPath() {
		// pass Global DAO to MyPath for lazy load and after-query if needed
		if (path != null) {
			path.setDao(getGlobalDAO().getMyPathDAO());
		}
		return path;
	}

	public String getTitle() {
		// lazy load
		if (title != null) {
			return title;
		}
		// required
		if (getPath() == null) {
			return null;
		}
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(getPath().getAbsPath());
		title = retriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		if (title == null) {
			title = "";
		}
		return title;

	}

	@Override
	public Integer insert() {
		return getDao().insert(this);// new id
	}

	@Override
	public Boolean loadAllProperties() {
		getPath();
		getAlbum();
		getArtist();
		getBirate();
		getDuration();
		getFormat();
		getId();
		getTitle();

		return true;
	}

	@Override
	public Boolean reset() {
		title = null;
		album = null;
		bitrate = null;
		duration = null;
		format = null;
		// reset path
		if (path != null) {
			path.reset();
		}
		return true;
	}

	public void setAlbum(MyAlbum album) {
		this.album = album;
	}

	public void setArtist(MyArtist artist) {
		this.artist = artist;
	}

	public void setBitrate(MyBitrate bitrate) {
		this.bitrate = bitrate;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setFormat(MyFormat format) {
		this.format = format;
	}

	public void setPath(MyPath absPath) {
		// init new _path
		path = absPath;
		// then reset
		reset();
	}

	public void setPath(String absPath) {
		// init new _path
		path = new MyPath();
		path.setAbsPath(absPath);
		// then reset
		reset();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Boolean update() {
		// TODO Auto-generated method stub
		return null;
	}
}
