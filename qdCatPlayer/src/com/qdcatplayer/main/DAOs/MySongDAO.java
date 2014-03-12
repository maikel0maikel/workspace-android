package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.objects.MyAlbum;
import com.qdcatplayer.main.objects.MySong;

/**
 * Lam viec truc tiep voi doi tuong MySong
 * trong CSDL de truy van ra danh sach cac doi tuong
 * @author quocdunginfo
 *
 */
public class MySongDAO {
	private MyDBManager _mn=null;//only one
    public MySongDAO(Context ctx) {
		//use init instead
    	init(ctx);
	}
    /**
     * Khoi tao du lieu tu Context, MyDBHelper duoc tao tu dong
     * @param ctx
     * @return
     */
    public Boolean init(Context ctx) {
    	//cached
    	if(_mn!=null && getHelper()!=null)
    	{
    		return true;
    	}
    	_mn = new MyDBManager();
		_mn.getHelper(ctx);//init helper inside MyDBManager
		return true;
    }
    public ArrayList<MySong> getAll()
	{
		ArrayList<MySong> re = new ArrayList<MySong>();
		try {
            List<MySong> tmp = getDao().queryForAll();
            re.addAll(tmp);
            return re;
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
            return null;
        }
	}
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
	public ArrayList<MySong> getByTitle(String title)
	{
		ArrayList<MySong> re = new ArrayList<MySong>();
		
		
		return re;
	}
	public ArrayList<MySong> getByAlbum(MyAlbum album)
	{
		ArrayList<MySong> re = new ArrayList<MySong>();
		
		
		return re;
	}
	public ArrayList<MySong> getByAbsPath(Integer id)
	{
		ArrayList<MySong> re = new ArrayList<MySong>();
		
		
		return re;
	}
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
	public int update(MySong obj)
    {
        try {
        	//triger lazy load to get all data need to be inserted
        	obj.loadAllProperties();
        	return getDao().update(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
	public int delete(MySong obj)
    {
        try {
        	//must have _id first
        	return getDao().delete(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
	public Boolean release()
	{
		//release
		_mn.releaseHelper();
		_mn=null;
		return true;
	}
	private Dao<MySong,Integer> getDao()
	{
		return _mn.getHelper().getMySongDAO();
	}
	private MySQLiteHelper getHelper()
	{
		return _mn.getHelper();
	}
}
