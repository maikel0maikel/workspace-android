package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.Entities.MyPlayList;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.Entities.MySong_MyPlayList;
import com.qdcatplayer.main.Libraries.MyFileHelper;

public class MyPlayListDAO extends _MyDAOAbstract<MyPlayListDAO, MyPlayList>
implements _MyDAOInterface<MyPlayListDAO, MyPlayList>
{
	/**
	 * Neu khong chi dinh GlobalDAO thi dat g=null (se tu dong tao)
	 * @param ctx
	 * @param g
	 */
	public MyPlayListDAO(Context ctx, GlobalDAO g) {
		super(ctx, g);
	}
	
	@Override
	public RuntimeExceptionDao<MyPlayList, Integer> getDao() {
		// TODO Auto-generated method stub
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMyPlayListDAO();
		}
		return null;
	}

	@Override
	public ArrayList<MyPlayList> getAll() {
		ArrayList<MyPlayList> re = new ArrayList<MyPlayList>();
        List<MyPlayList> tmp = getDao().queryForAll();
        for(MyPlayList item:tmp)
        {
        	item.setDao(this);
        	item.setLoaded(true);
        }
        re.addAll(tmp);
        return re;
	}

	@Override
	public MyPlayList getById(Integer id) {
		MyPlayList re = getDao().queryForId(id);
		re.setDao(this);
		re.setLoaded(true);//very importance
		return re;
	}
	
	@Override
	public Boolean update(MyPlayList obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(MyPlayList obj) {
		//neu object chua co trong DB thi goi super insert
		if(getSource()==MySource.DB_SOURCE)
		{
			try {
				MyPlayList tmp = getDao().queryBuilder().where().eq(MyPlayList.NAME_F, obj.getName()).queryForFirst();
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
		//DO NOT SUPPORT DISK SOURCE
		return -1;
	}

	public ArrayList<MySong> getSongs(MyPlayList obj) {
		ArrayList<MySong> re = new ArrayList<MySong>();
		MySong song=null;
		if(getSource()==MySource.DB_SOURCE)
		{
			try {
				List<MySong_MyPlayList> tmp =
						getGlobalDAO().
						getMySong_MyPlayListDAO().getDao().queryForEq(MySong_MyPlayList.PLAYLIST_ID, obj.getId());
				for(MySong_MyPlayList item:tmp)
				{
					song=item.getSong();
					song.setDao(getGlobalDAO().getMySongDAO());
					song.setLoaded(false);//not true because MySong_MyPlayList only load ID
					re.add(song);
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
