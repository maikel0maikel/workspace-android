package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;

public abstract class _MyDAOAbstract<T> implements _MyDAOInterface<T> {
	private MyDBManager _mn=null;
	public _MyDAOAbstract(Context ctx) {
		init(ctx);
	}
	@Override
	public Boolean release() {
		if(_mn!=null)
		{
			_mn.releaseHelper();
			_mn=null;
			return true;
		}
		return true;
	}
	/**
     * Khoi tao du lieu tu Context, MyDBHelper duoc tao tu dong
     * @param ctx
     * @return
     */
	@Override
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

	@Override
	public MySQLiteHelper getHelper() {
		if(_mn==null)
		{
			return null;
		}
		return _mn.getHelper();
	}
	@Override
	public MyDBManager getManager() {
		// TODO Auto-generated method stub
		return _mn;
	}
}
