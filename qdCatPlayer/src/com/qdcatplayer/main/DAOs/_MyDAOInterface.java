package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;

public interface _MyDAOInterface<T, K> {
	public Boolean release();

	public Boolean init(Context ctx, GlobalDAO g);

	public MySQLiteHelper getHelper();

	public MyDBManager getManager();

	public GlobalDAO getGlobalDAO();

	public RuntimeExceptionDao<K, Integer> getDao();

	public void setSource(Integer source);

	public Integer getSource();

	public void load(K from_);

	public ArrayList<K> getAll();

	public K getById(Integer id);

	public Integer insert(K obj);

	public Boolean update(K obj);

	public Boolean delete(K obj);
}
