package com.qdcatplayer.main.Entities;

import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.DAOs.MySource;

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
public class MySong extends _MyEntityAbstract<MySongDAO, MySong> {
	public static final String ALBUM_ID = "album_id";

	/**
	 * Ban dau tat ca thuoc tinh deu la null va loaded=false
	 * Mot khi co loi goi load mot object thi se goi toi loadAllProperties
	 * de dua tat ca truoc du lieu vao
	 */
	public static final String PATH_ID = "path_id";

	public static final String ARTIST_ID = "artist_id";

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

	@DatabaseField(canBeNull = false, foreign = true)
	private MyPath path = null;

	/**
	 * Because 1 song only has 1 title, and common music player doesn't group
	 * songs by title too, no need to create Object String better
	 */
	@DatabaseField(canBeNull=false)
	private String title = null;

	public MySong() {
	}
	
	/**
	 * Neu khoi tao tu ID thi bat buoc phai set DB SOURCE
	 * Neu da co san obj voi path, absPath thi SOURCE nao cung duoc
	 * @param removeFromDisk
	 * @return
	 */
	public Boolean delete(Boolean removeFromDisk)
	{
		return getDao().delete(this, true);
	}
	/*
	 * Ho tro pass DAO neu FK chua co, do su dung getById tu lop DAO
	 */
	public MyAlbum getAlbum() {
		super.load();
		if(album!=null && album.getDao()==null)//very importance, vi super.load khong ho tro pass DAO khi load tu DB SOURCE
		{
			album.setDao(getGlobalDAO().getMyAlbumDAO());
		}
		return album;
	}

	public MyArtist getArtist() {
		super.load();
		if(artist!=null && artist.getDao()==null)
		{
			artist.setDao(getGlobalDAO().getMyArtistDAO());
		}
		return artist;
	}

	public MyBitrate getBirate() {
		super.load();
		if(bitrate!=null && bitrate.getDao()==null)
		{
			bitrate.setDao(getGlobalDAO().getMyBitrateDAO());
		}
		return bitrate;
	}

	public Long getDuration() {
		super.load();
		return duration;
	}

	public MyFormat getFormat() {
		super.load();
		if(format!=null && format.getDao()==null)
		{
			format.setDao(getGlobalDAO().getMyFormatDAO());
		}
		return format;
	}

	public MyPath getPath() {
		if(getGlobalDAO().getSource()!=MySource.DISK_SOURCE)
		{
			super.load();
		}
		if(path!=null && path.getDao()==null)
		{
			path.setDao(getGlobalDAO().getMyPathDAO());
		}
		return path;
	}

	public String getTitle() {
		//de phong truong hop setTitle bang tay khi edit Tag => update
		if(title!=null)
		{
			return title;
		}
		super.load();
		return title;

	}
	/**
	 * Chi ho tro DISK SOURCE
	 */
	/*
	@Override
	public Integer insert() {
		if(getGlobalDAO().getSource()==MySource.DISK_SOURCE)
		{
			//force to update all value first
			reset();
			super.load();
			return getDao().insert(this);//new id
		}
		return -1;
	}
	*/
	
	@Override
	public void reset() {
		super.reset();
		
		title = null;
		album = null;
		bitrate = null;
		duration = null;
		format = null;
		artist = null;
		//do not reset path, id
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
		path = absPath;
	}

	public void setPath(String absPath_)
	{
		path = new MyPath();
		path.setAbsPath(absPath_);
		path.setDao(getGlobalDAO().getMyPathDAO());//importance
	}

	public void setTitle(String title) {
		this.title = title==null?"":title;
	}
	/**
	 * Check 1 bai hat con ton ton tren DISK
	 * @return
	 */
	public Boolean isOnDisk()
	{
		if(getPath()==null)
		{
			return false;
		}
		return getPath().isOnDisk();
	}
	/**
	 * Kiem tra bai hat co thuoc trong mot Songs List hay khong
	 * dua tren absPath
	 * @param list
	 * @return
	 */
	public Boolean isSongInList(ArrayList<MySong> list)
	{
		for(MySong item:list)
		{
			if(item.getPath().getAbsPath().equals(this.getPath().getAbsPath()))
			{
				return true;
			}
		}
		return false;
	}
}
