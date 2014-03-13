package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.qdcatplayer.main.entities.MyFolder;

public class MyFolderDAO extends _MyDAOAbstract<MyFolder> {

	public MyFolderDAO(Context ctx, GlobalDAO g) {
		super(ctx,g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Dao<MyFolder, Integer> getDao() {
		// TODO Auto-generated method stub
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
	public int insert(MyFolder obj) {
		// TODO Auto-generated method stub
		return 0;
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

}
