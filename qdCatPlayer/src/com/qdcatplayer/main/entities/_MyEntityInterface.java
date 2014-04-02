package com.qdcatplayer.main.Entities;

import java.util.ArrayList;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs._MyDAOInterface;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.Entities.MySong;

import android.content.Context;

public interface _MyEntityInterface<T,K> {
	public Integer getId();
	public void setId(Integer id);
	/**
	 * Load all properties
	 */
	public void load();
	public Boolean isLoaded();
	public void setLoaded(Boolean loaded);
	public void reset();
	public void setDao(T dao);
	public T getDao();
	public Integer insert();
	public Boolean update();
	public Boolean delete();
}
