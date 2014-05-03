package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;
import org.cmc.music.myid3.MyID3v1;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import com.j256.ormlite.dao.RuntimeExceptionDao;
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
        	item.setLoaded(true);//very importance
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
	/**
	 * 
	 */
	@Override
	public Boolean update(MySong obj)
    {
		//backup source state
		Integer bk_source = obj.getGlobalDAO().getSource();
		//first change to DB_SOURCE first to ensure mp3 file not deleted
		obj.getGlobalDAO().setSource(MySource.DB_SOURCE);
		//get tmp info for preservation
		String absPath = obj.getPath().getAbsPath();
		String album = obj.getAlbum().getName();
		String artist = obj.getArtist().getName();
		String title = obj.getTitle();
		//force to insert FK first
		obj.getAlbum().insert();
		obj.getArtist().insert();
		//call update to DB
		int re = getDao().update(obj);
		//update tag info to mp3 file on disk
		File src = new File(absPath);
		MusicMetadataSet src_set;
		try {
			//read metaset for holder
			src_set = new MyID3().read(src);
			if (src_set == null) // perhaps no metadata
			{
				return false;
			}
			else
			{
				//create metavalues
				MusicMetadata metadata = new MusicMetadata("");
				metadata.setArtist(artist);
				metadata.setAlbum(album);
				metadata.setSongTitle(title);
				new MyID3().update(src, src_set, metadata);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		
		//-----------------------------------update but clone new id
		/*
		//backup source state
		Integer bk_source = obj.getGlobalDAO().getSource();
		//first change to DB_SOURCE first to ensure mp3 file not deleted
		obj.getGlobalDAO().setSource(MySource.DB_SOURCE);
		//get tmp info for preservation
		String absPath = obj.getPath().getAbsPath();
		String album = obj.getAlbum().getName();
		String artist = obj.getArtist().getName();
		String title = obj.getTitle();
		//update tag info to mp3 file on disk
		File src = new File(obj.getPath().getAbsPath());
		MusicMetadataSet src_set;
		try {
			//read metaset for holder
			src_set = new MyID3().read(src);
			if (src_set == null) // perhaps no metadata
			{
				return false;
			}
			else
			{
				//create metavalues
				MusicMetadata metadata = new MusicMetadata("");
				metadata.setArtist(artist);
				metadata.setAlbum(album);
				metadata.setSongTitle(title);
				new MyID3().update(src, src_set, metadata);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		//delete current in DB, do not remove from disk
		obj.delete();
		
		//clone new one for clean insert
		//	switch to DISK SOURCE and then load mp3 file agian => re-insert to DB
		MySong new_obj = new MySong();
		new_obj.setDao(this);
		new_obj.getGlobalDAO().setSource(MySource.DISK_SOURCE);
		new_obj.setPath(absPath);
		new_obj.insert();
		//copy source from obj=>new_obj
		new_obj.getGlobalDAO().setSource(bk_source);
		obj = new_obj;
		return true;
		*/
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
		Boolean ID3_NATIVE_ANDROID = false;
		if(getSource()==MySource.DB_SOURCE)
		{
			super.load(obj);
		}
		else if(getSource()==MySource.DISK_SOURCE)
		{
			if(ID3_NATIVE_ANDROID)
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
			//use external myID3 library
			else
			{
				File src = new File(obj.getPath().getAbsPath());
				MusicMetadataSet src_set;
				try {
					src_set = new MyID3().read(src);
					if (src_set == null) // perhaps no metadata
					{
						obj.setTitle("");
						obj.setAlbum(new MyAlbum(""));
						obj.setArtist(new MyArtist(""));
						obj.setBitrate(new MyBitrate("128"));
						obj.setFormat(new MyFormat("MP3"));
						obj.setDuration(0l);
					}
					else
					{
						IMusicMetadata metadata = src_set.getSimplified();
						obj.setArtist(new MyArtist(metadata.getArtist()));
						obj.setAlbum(new MyAlbum(metadata.getAlbum()));
						obj.setBitrate(new MyBitrate("128"));
						obj.setFormat(new MyFormat("MP3"));
						obj.setTitle(metadata.getSongTitle());
						obj.setDuration(
								MyNumberHelper.stringToLong(metadata.getDurationSeconds())
						);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // read metadata
			}
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
