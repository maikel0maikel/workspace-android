package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.objects.MyAlbum;

public class MyAlbumDAO extends _MyDAOAbstract<MyAlbum> {

	public MyAlbumDAO(Context ctx) {
		super(ctx);
	}

	@Override
	public Dao<MyAlbum, Integer> getDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MyAlbum> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyAlbum getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(MyAlbum obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean update(MyAlbum obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(MyAlbum obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
