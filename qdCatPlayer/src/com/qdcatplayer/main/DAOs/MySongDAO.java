package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MyArtist;
import com.qdcatplayer.main.entities.MyBitrate;
import com.qdcatplayer.main.entities.MyFormat;
import com.qdcatplayer.main.entities.MyPath;
import com.qdcatplayer.main.entities.MySong;
import com.qdcatplayer.main.libraries.MyNumberHelper;

/**
 * Lam viec truc tiep voi doi tuong MySong
 * trong CSDL de truy van ra danh sach cac doi tuong
 * @author quocdunginfo
 *
 */
public class MySongDAO extends _MyDAOAbstract<MySongDAO, MySong>
implements _MyDAOInterface<MySongDAO,MySong>
{
    //_mn member inherted from parent class
	public MySongDAO(Context ctx, GlobalDAO g) {
    	super(ctx,g);
	}
    @Override
    public ArrayList<MySong> getAll()
	{
		ArrayList<MySong> re = new ArrayList<MySong>();
        List<MySong> tmp = getDao().queryForAll();
        for(MySong item:tmp)
        {
        	item.setDao(this);
        }
        re.addAll(tmp);
        return re;
	}
    @Override
	public MySong getById(Integer id)
	{
		return getDao().queryForId(id);	
	}

	@Override
	public Integer insert(MySong obj)
	{
		if(getDao()==null)//qd fail
		{
			return -1;
		}
		try{
			MyPath tmp = getGlobalDAO().getMyPathDAO()
					.getDao().queryBuilder()
					.where().eq(MyPath.ABSPATH_F, obj.getPath().getAbsPath())
					.queryForFirst();
			//neu Path da ton tai thi bo qua insert luon
			if(tmp!=null)
			{
				obj.getPath().setId(tmp.getId());
				return -1;
			}
			
			//create FK First
			obj.getAlbum().insert();
			obj.getArtist().insert();
			obj.getBirate().insert();
			obj.getFormat().insert();
			obj.getPath().insert();
			int re = getDao().create(obj);
			return re;
		}catch(Exception e)
		{
			//e.printStackTrace();
			return -1;//mac du insert thanh cong thi van bi quang Exception
		}
        
	}
	@Override
	public Boolean update(MySong obj)
    {
    	
		getDao().update(obj);
    	return true;
        
    }
	@Override
	public Boolean delete(MySong obj)
    {
        if(getSource()==MySource.DB_SOURCE)
        {
        	//delete song first
			MyPath path = obj.getPath();
			getDao().delete(obj);
			//then delete path
			if(path!=null)
			{
				path.delete();
			}
			return true;
        }
        else if(getSource()==MySource.DISK_SOURCE)
        {
        	//delete by absPath
        	String absPath = obj.getPath().getAbsPath();
        	File f = new File(absPath);
        	if(f!=null && f.isFile())
        	{
        		f.delete();
        		return true;
        	}
        	return false;
        }
        return false;
    }
	@Override
	public RuntimeExceptionDao<MySong,Integer> getDao()
	{
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMySongDAO();
		}
		return null;
	}
	public MyAlbum getAlbum(MySong obj) {
		if(getSource()==MySource.DISK_SOURCE)
		{
			// required
			if (obj.getPath() == null) {
				return null;
			}
	
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			retriever.setDataSource(obj.getPath().getAbsPath());
			String tmp = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
			MyAlbum album = new MyAlbum(tmp);
			album.setDao(getGlobalDAO().getMyAlbumDAO());
			obj.setAlbum(album);
			return album;
		}
		else if(getSource()==MySource.DB_SOURCE)
		{
			try {
				String[] tmp =  getDao().queryBuilder()
				.selectColumns(MySong.ALBUM_ID)
				.where()
				.eq(MySong.ID_F, obj.getId())
				.queryRawFirst();
				
				MyAlbum tmp2 = getGlobalDAO().getMyAlbumDAO().getDao()
						.queryForId(Integer.parseInt(tmp[0]));
				obj.setAlbum(tmp2);
				return tmp2;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new MyAlbum();
		}
		return null;
	}
	public String getTitle(MySong obj) {
		if(getSource()==MySource.DB_SOURCE)
		{
			if(obj.getId()>0)
			{
				getDao().refresh(obj);
				return obj.getTitle();
			}
		}
		else if(getSource()==MySource.DISK_SOURCE)
		{
			// required
			if (obj.getPath() == null) {
				return "";
			}
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			retriever.setDataSource(obj.getPath().getAbsPath());
			String title = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			if (title == null) {
				title = "";
			}
			return title;
		}
		return "";
	}
	public MyPath getPath(MySong obj) {
		if(getSource()==MySource.DB_SOURCE)
		{
			getDao().refresh(obj);
			if(obj.getPath()==null)
			{
				return null;
			}
			return obj.getPath();
		}
		//do not support get Path from DISK source
		return null;
	}
	@Override
	public void load(MySong obj) {
		if(getSource()==MySource.DB_SOURCE)
		{
			getDao().refresh(obj);
		}
		else if(getSource()==MySource.DISK_SOURCE)
		{
			//load tat ca thong tin tu DISK len
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			retriever.setDataSource(obj.getPath().getAbsPath());
			
			String tmp = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			obj.setDuration(
					MyNumberHelper.stringToLong(tmp)
					);
			
			tmp = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
			MyAlbum album = new MyAlbum(tmp);
			album.setDao(getGlobalDAO().getMyAlbumDAO());
			obj.setAlbum(album);
			
			tmp = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			obj.setTitle(tmp);
			
			tmp = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
			MyArtist artist = new MyArtist(tmp);
			artist.setDao(getGlobalDAO().getMyArtistDAO());
			obj.setArtist(artist);
			
			//tmp = retriever
			//		.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
			MyBitrate bitrate = new MyBitrate("128");
			bitrate.setDao(getGlobalDAO().getMyBitrateDAO());
			obj.setBitrate(bitrate);
			
			MyFormat format = new MyFormat("MP3");
			format.setDao(getGlobalDAO().getMyFormatDAO());
			obj.setFormat(format);
			
			//DONE
		}
	}
}
