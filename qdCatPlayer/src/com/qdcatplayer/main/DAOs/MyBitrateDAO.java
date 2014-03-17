package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyBitrate;

public class MyBitrateDAO extends _MyDAOAbstract<MyBitrate> {

	public MyBitrateDAO(Context ctx, GlobalDAO g) {
		super(ctx,g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RuntimeExceptionDao<MyBitrate, Integer> getDao() {
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMyBitrateDAO();
		}
		return null;
	}

	@Override
	public ArrayList<MyBitrate> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyBitrate getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean update(MyBitrate obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(MyBitrate obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
