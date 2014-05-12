package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MySong;

public class MyArtistDAO extends _MyDAOAbstract<MyArtistDAO, MyArtist>
		implements _MyDAOInterface<MyArtistDAO, MyArtist> {

	public MyArtistDAO(Context ctx, GlobalDAO g) {
		super(ctx, g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RuntimeExceptionDao<MyArtist, Integer> getDao() {
		if (getManager() != null && getHelper() != null) {
			return getHelper().getMyArtistDAO();
		}
		return null;
	}

	@Override
	public ArrayList<MyArtist> getAll() {
		ArrayList<MyArtist> re = new ArrayList<MyArtist>();
		List<MyArtist> tmp = getDao().queryForAll();
		for (MyArtist item : tmp) {
			item.setDao(this);
			item.setLoaded(true);
		}
		re.addAll(tmp);
		return re;
	}

	@Override
	public MyArtist getById(Integer id) {
		MyArtist re = getDao().queryForId(id);
		re.setDao(this);
		re.setLoaded(true);// very importance
		return re;
	}

	@Override
	public Boolean update(MyArtist obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(MyArtist obj) {
		// neu object chua co trong DB thi goi super insert
		try {
			MyArtist tmp = getDao().queryBuilder().where()
					.eq(MyArtist.NAME_F, obj.getName()).queryForFirst();
			if (tmp == null) {
				super.insert(obj);
			} else {
				obj.setId(tmp.getId());
				obj.reset();
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public ArrayList<MySong> getSongs(MyArtist obj) {
		ArrayList<MySong> re = new ArrayList<MySong>();
		if (getSource() == MySource.DB_SOURCE) {
			try {
				/*
				 * List<MySong> tmp = getGlobalDAO().getMySongDAO().getDao()
				 * .queryBuilder().where().eq(MySong.ALBUM_ID, obj.getId())
				 * .query();
				 */
				List<MySong> tmp = getGlobalDAO().getMySongDAO().getDao()
						.queryForEq(MySong.ARTIST_ID, obj.getId());
				for (MySong item : tmp) {
					item.setDao(getGlobalDAO().getMySongDAO());
					item.setLoaded(true);
					re.add(item);
				}
				return re;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// do not support DISK SOURCE
		return re;
	}
}
