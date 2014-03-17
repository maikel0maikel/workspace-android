package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MySong;

public class MyAlbumDAO extends _MyDAOAbstract<MyAlbum> {
	/**
	 * Neu khong chi dinh GlobalDAO thi dat g=null (se tu dong tao)
	 * @param ctx
	 * @param g
	 */
	public MyAlbumDAO(Context ctx, GlobalDAO g) {
		super(ctx, g);
	}

	@Override
	public RuntimeExceptionDao<MyAlbum, Integer> getDao() {
		// TODO Auto-generated method stub
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMyAlbumDAO();
		}
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
	public Boolean update(MyAlbum obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(MyAlbum obj) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer insert(MyAlbum obj) {
		//neu object chua co trong DB thi goi super insert
		try {
			MyAlbum tmp = getDao().queryBuilder().where().eq(MyAlbum.NAME_F, obj.getName()).queryForFirst();
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

	public String getName(MyAlbum obj) {
		if(getSource()==MySource.DB_SOURCE)
		{
			if(obj.getId() > 0)
			{
				getDao().refresh(obj);
				return obj.getName();
			}
		}
		//do not support DISK SOURCE
		return "";
	}

	public ArrayList<MySong> getSongs(MyAlbum obj) {
		ArrayList<MySong> re = new ArrayList<MySong>();
		if(getSource()==MySource.DB_SOURCE)
		{
			try {
				List<MySong> tmp = getGlobalDAO().getMySongDAO().getDao()
						.queryBuilder().where().eq(MySong.ALBUM_ID, obj.getId())
						.query();
				
				for(MySong item:tmp)
				{
					item.setDao(getGlobalDAO().getMySongDAO());
					re.add(item);
				}
				return re;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//do not support DISK SOURCE
		return new ArrayList<MySong>();
	}
}
