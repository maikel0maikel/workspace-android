package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MySong;
import com.qdcatplayer.main.entities._MyEntityAbstract;
import com.qdcatplayer.main.entities._MyEntityInterface;

public abstract class _MyDAOAbstract<T,K> implements _MyDAOInterface<T,K>, _GlobalDAOInterface {
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
	@Override
	public Integer insert(K obj) {
		if(getDao()==null)
		{
			return -1;
		}
		if(getSource()==MySource.DISK_SOURCE)
		{
			try{
				getDao().create(obj);
				return 1;
			}catch(Exception e){
				//object co truong name trung voi record trong CSDL do unique
				//insert khong duoc return ma loi
				e.printStackTrace();
				return -1;
			}
		}
		return -1;
	}
	@Override
	public void setSource(Integer source_) {
		if(getGlobalDAO()!=null)
		{
			getGlobalDAO().setSource(source_);
		}
	}
	@Override
	public Integer getSource() {
		if(getGlobalDAO()!=null)
		{
			return getGlobalDAO().getSource();
		}
		return 0;//by default
	}
	/**
	 * getDao().refresh(obj);
	 * DO NOT SUPPORT LOAD FROM DISK SOURCE
	 * IF YOU WANT LOAD FROM DISK SOURCE YOU CAN Override
	 */
	@Override
	public void load(K obj) {
		if(getSource()==MySource.DB_SOURCE)
		{
			if(getDao().refresh(obj)==1)//load thanh cong
			{
				((_MyEntityAbstract<T, K>)obj).setLoaded(true);
			}
		}
		//do not support load from DISK SOURCE
	}
	/**
	 * DO NOT SUPPORT DISK SOURCE
	 * getDao().delete(obj);
	 */
	@Override
	public Boolean delete(K obj) {
		if(getSource()==MySource.DB_SOURCE)
		{
			getDao().delete(obj);
			return true;
		}
		//DO NOT SUPPORT DISK SOURCE
		return false;
	}
	@Override
	public Boolean update(K obj) {
		// TODO Auto-generated method stub
		return null;
	}
}
