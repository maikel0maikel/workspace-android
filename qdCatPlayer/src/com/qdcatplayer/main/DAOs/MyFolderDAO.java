package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.objects.MyFolder;

public class MyFolderDAO extends _MyDAOAbstract<MyFolder> {

	public MyFolderDAO(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dao<MyFolder, Integer> getDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MyFolder> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyFolder getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(MyFolder obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean update(MyFolder obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(MyFolder obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
