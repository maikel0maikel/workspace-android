package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.entities.MyFolder;
import com.qdcatplayer.main.entities.MyPath;

public class MyPathDAO extends _MyDAOAbstract<MyPath> {

	public MyPathDAO(Context ctx, GlobalDAO g) {
		super(ctx,g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dao<MyPath, Integer> getDao() {
		// TODO Auto-generated method stub
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
	public int insert(MyPath obj) {
		// TODO Auto-generated method stub
		return 0;
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
