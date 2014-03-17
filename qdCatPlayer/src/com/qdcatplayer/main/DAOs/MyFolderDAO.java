package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.entities.MyFolder;
import com.qdcatplayer.main.entities.MyPath;
import com.qdcatplayer.main.entities.MySong;
import com.qdcatplayer.main.libraries.MyFileHelper;

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
	public ArrayList<MySong> getAllRecursiveSongs(MyFolder obj)
	{
		ArrayList<MySong> recursiveSongs = new ArrayList<MySong>();
		return _loadRecursiveSongs(obj, recursiveSongs);
	}
	private ArrayList<MySong> _loadRecursiveSongs(MyFolder root, ArrayList<MySong> recursiveSongs) {
		if (root == null) {
			return recursiveSongs;
		}
		// add direct first
		for (MySong item : root.getChildSongs()) {
			recursiveSongs.add(item);
		}
		// finish on this node
		if (root.getChildFolders().size() <= 0) {
			return recursiveSongs;
		}
		for (MyFolder item : root.getChildFolders()) {
			_loadRecursiveSongs(item, recursiveSongs);
		}
		return recursiveSongs;
	}

	public ArrayList<MySong> getChildSongs(MyFolder obj) {
		ArrayList<MySong> childsSong = new ArrayList<MySong>();
		//set from SOURCE
		if(getSource()==MySource.DISK_SOURCE)
		{
			File f = new File(obj.getAbsPath());
			File[] tmp = f.listFiles();
			// no childs
			if (tmp == null) {
				return childsSong;
			}
			// has childs
			MySong tmps;
			for (File item : tmp) {
				if (item.isFile()
						&& MyFileHelper.isSoundFile(item.getAbsolutePath())) {
					tmps = new MySong(item.getAbsolutePath());
					//set DAO
					tmps.setDao(getGlobalDAO().getMySongDAO());
					childsSong.add(tmps);
				}
			}
		}
		else if(getSource()==MySource.DB_SOURCE)
		{
			try {
				//get folder from DB first
				MyFolder fd =
						getDao().queryBuilder()
						.where().
						eq(MyFolder.ABSPATH_F, obj.getAbsPath()).queryForFirst();
				if(fd==null)
				{
					return childsSong;
				}
				//get all Path belong to this
				List<MyPath> paths = getGlobalDAO().getMyPathDAO().getDao()
						.queryBuilder().where()
						.eq(MyPath.P_FOLDER_ID, fd.getId())
						.query();
				for(MyPath item:paths)
				{
					MySong song_tmp = getGlobalDAO().getMySongDAO().getDao()
							.queryBuilder().where()
							.eq(MySong.PATH_ID, item.getId())
							.queryForFirst();
					if(song_tmp!=null)
					{
						song_tmp.setDao(getGlobalDAO().getMySongDAO());
						childsSong.add(song_tmp);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return childsSong;
			}
		}
		return childsSong;
	}
}
