package com.qdcatplayer.main.DAOs;

import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.Entities.MyFormat;

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
		MyFormat obj = getDao().queryForId(id);
		obj.setDao(this);
		obj.setLoaded(true);
		return obj;
	}

	@Override
	public Integer insert(MyFormat obj) {
		//neu object chua co trong DB thi goi super insert
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
}
