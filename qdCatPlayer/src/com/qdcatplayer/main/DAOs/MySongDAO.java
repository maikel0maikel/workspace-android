package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.entities.MyAlbum;
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
		try {
            List<MySong> tmp = getDao().queryForAll();
            for(MySong item:tmp)
            {
            	item.setDao(this);
            }
            re.addAll(tmp);
            return re;
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
            return null;
        }
	}
    @Override
	public MySong getById(Integer id)
	{
		try {
			return getDao().queryForId(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<MySong> getByAbsPath(Integer id)
	{
		ArrayList<MySong> re = new ArrayList<MySong>();
		
		
		return re;
	}
	@Override
	public int insert(MySong obj)
	{
		if(getDao()==null)
		{
			return 0;
		}
		
		try {
            //triger lazy load to get all data need to be inserted
			//obj.loadAllProperties();
			//call insert for 
            //create forieng first
			return getDao().create(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
	}
	@Override
	public Boolean update(MySong obj)
    {
        try {
        	//triger lazy load to get all data need to be inserted
        	obj.loadAllProperties();
        	getDao().update(obj);
        	return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	@Override
	public Boolean delete(MySong obj)
    {
        try {
        	//must have _id first
        	getDao().delete(obj);
        	return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	@Override
	public Dao<MySong,Integer> getDao()
	{
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMySongDAO();
		}
		return null;
	}
}
