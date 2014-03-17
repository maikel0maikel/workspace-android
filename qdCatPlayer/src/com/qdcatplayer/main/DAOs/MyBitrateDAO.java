package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyArtist;
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
	@Override
	public Integer insert(MyBitrate obj) {
		//neu object chua co trong DB thi goi super insert
		try {
			MyBitrate tmp = getDao().queryBuilder().where().eq(MyBitrate.VALUE_F, obj.getValue()).queryForFirst();
			if(tmp==null)
			{
				super.insert(obj);
			}
			else
			{
				obj.setId(tmp.getId());
				obj.reset();
			}
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
}
