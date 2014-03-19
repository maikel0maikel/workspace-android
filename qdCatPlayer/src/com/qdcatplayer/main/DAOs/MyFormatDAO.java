package com.qdcatplayer.main.DAOs;

import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyAlbum;
import com.qdcatplayer.main.entities.MyBitrate;
import com.qdcatplayer.main.entities.MyFormat;
import com.qdcatplayer.main.entities.MyPath;

public class MyFormatDAO extends _MyDAOAbstract<MyFormatDAO, MyFormat>
implements _MyDAOInterface<MyFormatDAO, MyFormat>
{

	public MyFormatDAO(Context ctx, GlobalDAO g) {
		super(ctx,g);
	}

	@Override
	public RuntimeExceptionDao<MyFormat, Integer> getDao() {
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMyFormatDAO();
		}
		return null;
	}

	@Override
	public ArrayList<MyFormat> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyFormat getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(MyFormat obj) {
		//neu object chua co trong DB thi goi super insert
		if(getSource()==MySource.DISK_SOURCE)
		{
			try {
				MyFormat tmp = getDao().queryBuilder().where().eq(MyFormat.EXTENSION_F, obj.getExtension()).queryForFirst();
				if(tmp==null)
				{
					super.insert(obj);
				}
				else
				{
					obj.setId(tmp.getId());
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
