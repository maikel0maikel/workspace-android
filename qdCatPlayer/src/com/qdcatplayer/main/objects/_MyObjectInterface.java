package com.qdcatplayer.main.objects;

import java.util.ArrayList;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.DAOs._MyDAOInterface;
import com.qdcatplayer.main.DBHelper.MyDBManager;
import com.qdcatplayer.main.DBHelper.MySQLiteHelper;
import com.qdcatplayer.main.objects.MySong;

import android.content.Context;

public interface _MyObjectInterface<T> {
	public Integer getId();
	public Boolean setId(Integer id);
	public Boolean loadAllProperties();
	public Boolean reset();
	public Boolean setDao(T dao);
	public T getDao();
	public Integer insert();
	public Boolean update();
	public Integer delete();
}
