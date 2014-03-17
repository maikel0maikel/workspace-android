package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MyPath;
import com.qdcatplayer.main.entities.MySong;

/**
 * Lam viec truc tiep voi doi tuong MySong
 * trong CSDL de truy van ra danh sach cac doi tuong
 * @author quocdunginfo
 *
 */
public class MySongDAO extends _MyDAOAbstract<MySong> {
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
	
	public ArrayList<MySong> getByAbsPath(Integer id)
	{
		ArrayList<MySong> re = new ArrayList<MySong>();
		
		
		return re;
	}
	@Override
	public Integer insert(MySong obj)
	{
		if(getDao()==null)//qd fail
		{
			return -1;
		}
		//load all direct properties
		try{
			MyPath tmp = getGlobalDAO().getMyPathDAO().getDao().queryBuilder().where().eq(MyPath.ABSPATH_F, obj.getPath().getAbsPath()).queryForFirst();
			if(tmp!=null)
			{
				obj.setId(tmp.getId());
				return 1;
			}
			obj.loadAllProperties();
			//create FK First
			obj.getAlbum().insert();
			obj.getArtist().insert();
			obj.getBirate().insert();
			obj.getFormat().insert();
			obj.getPath().insert();
			return getDao().create(obj);
		}catch(Exception e)
		{
			e.printStackTrace();
			return -1;//mac du insert thanh cong thi van bi quang Exception
		}
        
	}
	@Override
	public Boolean update(MySong obj)
    {
        //triger lazy load to get all data need to be inserted
    	obj.loadAllProperties();
    	getDao().update(obj);
    	return true;
        
    }
	@Override
	public Boolean delete(MySong obj)
    {
        getDao().delete(obj);
    	return true;
        
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
}
