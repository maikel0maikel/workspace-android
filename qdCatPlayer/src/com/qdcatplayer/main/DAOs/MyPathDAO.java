package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MyPath;
import com.qdcatplayer.main.Libraries.MyFileHelper;

public class MyPathDAO extends _MyDAOAbstract<MyPathDAO, MyPath> implements
		_MyDAOInterface<MyPathDAO, MyPath> {

	public MyPathDAO(Context ctx, GlobalDAO g) {
		super(ctx, g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RuntimeExceptionDao<MyPath, Integer> getDao() {
		if (getManager() != null && getHelper() != null) {
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
		if (getDao() == null) {
			return -1;
		}
		try {
			// neu path co roi trong he thong thi khong add
			if (obj.getId() != null && obj.getId() > 0) {
				return -1;
			}
			// neu path co roi trong he thong thi khong add ma
			// chi cap nhat id sang
			MyPath tmp = getDao().queryBuilder().where()
					.eq(MyPath.ABSPATH_F, obj.getAbsPath()).queryForFirst();
			if (tmp != null) {
				obj.setId(tmp.getId());
				return 1;
			}
			// create FK First
			if (obj.getParentFolder() != null) {
				obj.getParentFolder().insert();
			}
			// load all properties here and then insert ngay lap tuc
			obj.load();
			return getDao().create(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	@Override
	public Boolean update(MyPath obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public MyFolder getParentFolder(MyPath obj) {
		if (getSource() == MySource.DISK_SOURCE) {
			File f = new File(obj.getAbsPath());
			String parent = f.getParent();
			// no parent
			if (parent == null) {
				return null;
			}
			MyFolder tmp = new MyFolder();
			tmp.setAbsPath(parent);
			tmp.setDao(getGlobalDAO().getMyFolderDAO());
			return tmp;
		} else if (getSource() == MySource.DB_SOURCE) {
			getDao().refresh(obj);
			return obj.getParentFolder();// may loopback because parent_ready =
											// false
		}
		return null;
	}

	/**
	 * Chi ho tro DB SOURCE
	 * 
	 * @param obj
	 * @return
	 */
	public String getAbsPath(MyPath obj) {
		if (getSource() == MySource.DB_SOURCE) {
			if (obj.getId() > 0) {
				getDao().refresh(obj);
				return obj.getAbsPath();
			}
		}
		// do not support from Disk SOURCE
		// because of ID not present on Disk
		return "";// do not return null, loopback may occur
	}

	@Override
	public void load(MyPath obj) {
		if (getSource() == MySource.DISK_SOURCE) {
			if (obj.getAbsPath() == null || obj.getAbsPath().equals("")) {
				return;
			}
			obj.setFileName(MyFileHelper.getFileName(obj.getAbsPath(), false));

			obj.setFileExtension(MyFileHelper.getFileExtension(
					obj.getAbsPath(), false));
			obj.setLoaded(true);
		} else if (getSource() == MySource.DB_SOURCE) {
			super.load(obj);
		}
	}

}
