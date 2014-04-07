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
import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MyBitrate;
import com.qdcatplayer.main.Entities.MyFormat;
import com.qdcatplayer.main.Entities.MyPath;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.Libraries.MyFileHelper;
import com.qdcatplayer.main.Libraries.MyNumberHelper;

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
	/**
	 * Get all song in DB
	 * Only support DB SOURCE at this time
	 */
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
    /**
     * Ho tro pass DAO cho obj tra ve
     */
    @Override
	public MySong getById(Integer id)
	{
		MySong re = getDao().queryForId(id);
		re.setDao(this);
		re.setLoaded(true);//very importance
		return re;
	}

	@Override
	public Integer insert(MySong obj)
	{
		if(getDao()==null)
		{
			return -1;
		}
		if(getSource()==MySource.DISK_SOURCE)
		{
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
				e.printStackTrace();
				return -1;//mac du insert thanh cong thi van bi quang Exception
			}
		}
        return -1;
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
        	//get path for tmp use
			MyPath path = obj.getPath();
			//delete song first by call super method
			super.delete(obj);//#########################
			//then delete path
			if(path!=null)
			{
				path.delete();
			}
			return true;
        }
        else if(getSource()==MySource.DISK_SOURCE)
        {
        	if(obj.getPath()==null)
        	{
        		return false;
        	}
        	return MyFileHelper.removeFile(obj.getPath().getAbsPath());
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
	/**
	 * Ho tro set Loaded
	 * Khong ho tro pass DAO, phan quan ly pass DAO thuoc MySong
	 */
	@Override
	public void load(MySong obj) {
		if(getSource()==MySource.DB_SOURCE)
		{
			super.load(obj);
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
			obj.setAlbum(album);
			
			tmp = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			obj.setTitle(tmp);
			
			tmp = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
			MyArtist artist = new MyArtist(tmp);
			obj.setArtist(artist);
			
			//tmp = retriever
			//		.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
			MyBitrate bitrate = new MyBitrate("128");
			obj.setBitrate(bitrate);
			
			MyFormat format = new MyFormat("MP3");
			obj.setFormat(format);
			
			//DONE
			obj.setLoaded(true);
		}
	}
	/**
	 * Phai set DB SOURCE cho dao truoc
	 * @param obj
	 * @param removeFromDisk
	 * @return
	 */
	public Boolean delete(MySong obj, Boolean removeFromDisk) {
		if(obj==null || obj.getPath()==null)
		{
			return false;
		}
		if(removeFromDisk)
		{
			//neu obj da co san absPath va tu DISK
			if(getSource()==MySource.DISK_SOURCE
					&& obj.getPath()!=null
					&& obj.getPath().getAbsPath()!=null)
			{
				return delete(obj);
			}
			
			//Init new 2 layers delete script
			Integer bk = obj.getGlobalDAO().getSource();
			//try to switch to DB SOURCE first to get info
			obj.getGlobalDAO().setSource(MySource.DB_SOURCE);
			//call to load absPath in Path fisrt
			obj.getPath().getAbsPath();//trigger load absPath before delete from DB
			//call delete
			obj.delete();
			//absPath would still be existed after call above delete
			//change to DISK SOURCE
			obj.getGlobalDAO().setSource(MySource.DISK_SOURCE);
			obj.delete();
			//swicth to previous SOURCE
			obj.getGlobalDAO().setSource(bk);
			//finish
			return true;
		}
		else
		{
			return delete(obj);
		}
	}
}
