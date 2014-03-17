package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyFolder;
import com.qdcatplayer.main.entities.MyPath;

public class MyPathDAO extends _MyDAOAbstract<MyPath> {

	public MyPathDAO(Context ctx, GlobalDAO g) {
		super(ctx,g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RuntimeExceptionDao<MyPath, Integer> getDao() {
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMyPathDAO();
		}
		return null;
	}

	@Override
	public ArrayList<MyPath> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyPath getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(MyPath obj) {
		if(getDao()==null)//qd fail
		{
			return -1;
		}
		//load all direct properties
		try{
			obj.loadAllProperties();//FK and Direct Properties too
			//create FK First
			obj.getParentFolder().insert();
			return getDao().create(obj);
		}catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public Boolean update(MyPath obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(MyPath obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public MyFolder getParentFolder(MyPath myPath) {
		//see SOURCE to choose Provider 
		File f = new File(myPath.getAbsPath());
		String parent = f.getParent();
		//no parent
		if(parent==null)
		{
			return null;
		}
		return new MyFolder(parent);
	}

}
