package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;

public abstract class _MyDAOAbstract<T> implements _MyDAOInterface<T>, _GlobalDAOInterface {
	/**
	 * Lam viec truc tiep voi MyDBmanager, cac DAO nguyen thuy duoc Cache
	 * trong day
	 * MyDBHelper duoc truy xuat thong qua Manager nay
	 */
	private MyDBManager _mn=null;
	/*
	 * Trung tam quan ly cac custom DAO
	 * cung cap cho cac Objects su dung cross-space
	 * ho tro Cache cac custom DAO
	 */
	private GlobalDAO _globalDAO=null;
	public _MyDAOAbstract(Context ctx, GlobalDAO g) {
		init(ctx, g);
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
	public Boolean init(Context ctx, GlobalDAO g) {
		//cached
    	if(_mn!=null && getHelper()!=null)
    	{
    		return true;
    	}
    	_mn = new MyDBManager();
		_mn.getHelper(ctx);//init helper inside MyDBManager
		//init GlobalDAO if not ready but not generate any custom DAO inside
		//will be use when call from Object
		if(g!=null)
		{
			_globalDAO = g;
		}
		else
		{
			_globalDAO = new GlobalDAO(ctx);
		}
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
	@Override
	public GlobalDAO getGlobalDAO() {
		// TODO Auto-generated method stub
		return _globalDAO;
	}
}
