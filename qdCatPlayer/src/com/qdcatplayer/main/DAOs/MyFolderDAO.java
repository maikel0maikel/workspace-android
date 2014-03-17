package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyFolder;
import com.qdcatplayer.main.entities.MyPath;

public class MyFolderDAO extends _MyDAOAbstract<MyFolder> {

	public MyFolderDAO(Context ctx, GlobalDAO g) {
		super(ctx,g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RuntimeExceptionDao<MyFolder, Integer> getDao() {
		if(getManager()!=null && getHelper()!=null)
		{
			return getHelper().getMyFolderDAO();
		}
		return null;
	}

	@Override
	public ArrayList<MyFolder> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyFolder getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean update(MyFolder obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(MyFolder obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public MyFolder getParentFolder(MyFolder myFolder) {
		//see SOURCE to choose Provider 
		File f = new File(myFolder.getAbsPath());
		String parent = f.getParent();
		//no parent
		if(parent==null)
		{
			return null;
		}
		return new MyFolder(parent);
	}
	@Override
	public Integer insert(MyFolder obj) {
		if(getDao()==null)//qd fail
		{
			return -1;
		}
		//load all direct properties
		try{
			obj.loadAllProperties();//FK and Direct Properties too
			//create FK First
			if(obj.getParentFolder()!=null)
			{
				//xet coi folder nay da co trong he thong
				MyFolder tmp = getDao().queryBuilder().where().eq(MyFolder.ABSPATH_F, obj.getAbsPath()).queryForFirst();
				if(tmp!=null)
				{
					obj.setId(tmp.getId());
					return 1;
				}
				//goi insert folder cha truoc
				obj.getParentFolder().insert();
			}
			return getDao().create(obj);
		}catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
}
