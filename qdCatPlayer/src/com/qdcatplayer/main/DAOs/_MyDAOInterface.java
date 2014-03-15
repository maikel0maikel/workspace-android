package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.entities.MySong;

import android.content.Context;

public interface _MyDAOInterface<T> {
	public Boolean release();
	public Boolean init(Context ctx, GlobalDAO g);
	public MySQLiteHelper getHelper();
	public MyDBManager getManager();
	public GlobalDAO getGlobalDAO();
	public RuntimeExceptionDao<T,Integer> getDao();
	
	public ArrayList<T> getAll();
	public T getById(Integer id);
	public Integer insert(T obj);
	public Boolean update(T obj);
	public Boolean delete(T obj);
}
