package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import com.j256.ormlite.dao.Dao;
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
	public Dao<T,Integer> getDao();
	
	public ArrayList<T> getAll();
	public T getById(Integer id);
	public int insert(T obj);
	public Boolean update(T obj);
	public Boolean delete(T obj);
}
