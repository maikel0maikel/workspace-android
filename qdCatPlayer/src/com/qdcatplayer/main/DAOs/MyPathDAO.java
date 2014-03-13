package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.objects.MyPath;

public class MyPathDAO extends _MyDAOAbstract<MyPath> {

	public MyPathDAO(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dao<MyPath, Integer> getDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MyPath> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyPath getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(MyPath obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean update(MyPath obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(MyPath obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
