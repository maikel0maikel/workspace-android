package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.Entities.MySong_MyPlayList;

public class MySong_MyPlayListDAO extends
		_MyDAOAbstract<MySong_MyPlayListDAO, MySong_MyPlayList> implements
		_MyDAOInterface<MySong_MyPlayListDAO, MySong_MyPlayList> {
	/**
	 * Neu khong chi dinh GlobalDAO thi dat g=null (se tu dong tao)
	 * 
	 * @param ctx
	 * @param g
	 */
	public MySong_MyPlayListDAO(Context ctx, GlobalDAO g) {
		super(ctx, g);
	}

	@Override
	public RuntimeExceptionDao<MySong_MyPlayList, Integer> getDao() {
		// TODO Auto-generated method stub
		if (getManager() != null && getHelper() != null) {
			return getHelper().getMySong_MyPlayListDAO();
		}
		return null;
	}

	@Override
	public ArrayList<MySong_MyPlayList> getAll() {
		/*
		 * ArrayList<MyPlayList> re = new ArrayList<MyPlayList>();
		 * List<MyPlayList> tmp = getDao().queryForAll(); for(MyPlayList
		 * item:tmp) { item.setDao(this); item.setLoaded(true); }
		 * re.addAll(tmp); return re;
		 */
		return null;
	}

	@Override
	public MySong_MyPlayList getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean update(MySong_MyPlayList obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(MySong_MyPlayList obj) {
		/*
		 * //neu object chua co trong DB thi goi super insert
		 * if(getSource()==MySource.DISK_SOURCE) { try { MyPlayList tmp =
		 * getDao().queryBuilder().where().eq(MyPlayList.NAME_F,
		 * obj.getName()).queryForFirst(); if(tmp==null) { super.insert(obj); }
		 * else { obj.setId(tmp.getId()); obj.reset(); } return 1; } catch
		 * (Exception e) { e.printStackTrace(); return -1; } } //DO NOT SUPPORT
		 * DB SOURCE
		 */
		return -1;

	}

}
