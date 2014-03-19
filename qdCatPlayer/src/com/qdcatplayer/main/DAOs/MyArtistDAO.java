package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MyArtist;
import com.qdcatplayer.main.entities.MyBitrate;
import com.qdcatplayer.main.entities.MySong;

public class MyArtistDAO extends _MyDAOAbstract<MyArtistDAO, MyArtist>
implements _MyDAOInterface<MyArtistDAO, MyArtist>
{

	public MyArtistDAO(Context ctx, GlobalDAO g) {
		super(ctx, g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RuntimeExceptionDao<MyArtist, Integer> getDao() {
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMyArtistDAO();
		}
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
	public Boolean update(MyArtist obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(MyArtist obj) {
		//neu object chua co trong DB thi goi super insert
		if(getSource()==MySource.DISK_SOURCE)
		{
			try {
				MyArtist tmp = getDao().queryBuilder().where().eq(MyArtist.NAME_F, obj.getName()).queryForFirst();
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
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
		return -1;
	}
}
