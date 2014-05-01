package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MySong;

public class MyAlbumDAO extends _MyDAOAbstract<MyAlbumDAO, MyAlbum>
implements _MyDAOInterface<MyAlbumDAO, MyAlbum>
{
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
		ArrayList<MyAlbum> re = new ArrayList<MyAlbum>();
        List<MyAlbum> tmp = getDao().queryForAll();
        for(MyAlbum item:tmp)
        {
        	item.setDao(this);
        	item.setLoaded(true);
        }
        re.addAll(tmp);
        return re;
	}

	@Override
	public MyAlbum getById(Integer id) {
		MyAlbum re = getDao().queryForId(id);
		re.setDao(this);
		re.setLoaded(true);//very importance
		return re;
	}
	
	@Override
	public Boolean update(MyAlbum obj) {
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
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
	}

	public ArrayList<MySong> getSongs(MyAlbum obj) {
		ArrayList<MySong> re = new ArrayList<MySong>();
		if(getSource()==MySource.DB_SOURCE)
		{
			try {
				/*List<MySong> tmp = getGlobalDAO().getMySongDAO().getDao()
						.queryBuilder().where().eq(MySong.ALBUM_ID, obj.getId())
						.query();*/
				List<MySong> tmp = getGlobalDAO().getMySongDAO().getDao().queryForEq(MySong.ALBUM_ID, obj.getId());
				for(MySong item:tmp)
				{
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
		//do not support DISK SOURCE
		return re;
	}
}
