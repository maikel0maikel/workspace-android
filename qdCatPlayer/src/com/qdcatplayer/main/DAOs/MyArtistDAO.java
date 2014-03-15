package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyArtist;
import com.qdcatplayer.main.entities.MySong;

public class MyArtistDAO extends _MyDAOAbstract<MyArtist> {

	public MyArtistDAO(Context ctx, GlobalDAO g) {
		super(ctx, g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RuntimeExceptionDao<MyArtist, Integer> getDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MyArtist> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyArtist getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(MyArtist obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean update(MyArtist obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(MyArtist obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
