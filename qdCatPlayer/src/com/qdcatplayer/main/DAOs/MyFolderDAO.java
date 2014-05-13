package com.qdcatplayer.main.DAOs;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MyPath;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.Libraries.MyFileHelper;
import com.qdcatplayer.main.Libraries.MyStringHelper;

public class MyFolderDAO extends _MyDAOAbstract<MyFolderDAO, MyFolder>
		implements _MyDAOInterface<MyFolderDAO, MyFolder> {

	public MyFolderDAO(Context ctx, GlobalDAO g) {
		super(ctx, g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RuntimeExceptionDao<MyFolder, Integer> getDao() {
		if (getManager() != null && getHelper() != null) {
			return getHelper().getMyFolderDAO();
		}
		return null;
	}

	@Override
	public ArrayList<MyFolder> getAll() {
		ArrayList<MyFolder> re = new ArrayList<MyFolder>();
		List<MyFolder> tmp = getDao().queryForAll();
		for (MyFolder item : tmp) {
			item.setDao(this);
			item.setLoaded(true);
		}
		re.addAll(tmp);
		return re;
	}

	@Override
	public MyFolder getById(Integer id) {
		MyFolder re = getDao().queryForId(id);
		re.setDao(this);
		re.setLoaded(true);// very importance
		return re;
	}

	public MyFolder getByAbsPath(String absPath) {
		absPath = MyStringHelper.filterSQLSpecialAbsPath(absPath, "unknown", true);//for sure
		MyFolder obj;
		try {
			obj = getDao().queryBuilder().where()
					.eq(MyFolder.ABSPATH_F, absPath).queryForFirst();
			obj.setLoaded(true);
			obj.setDao(this);
			return obj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean update(MyFolder obj) {
		getDao().update(obj);
		return true;
	}

	public MyFolder getParentFolder(MyFolder myFolder) {
		// see SOURCE to choose Provider
		File f = new File(myFolder.getAbsPath());
		String parent = f.getParent();
		// no parent
		if (parent == null) {
			return null;
		}
		return new MyFolder(parent);
	}

	@Override
	public Integer insert(MyFolder obj) {
		try {
			// kiem tra xem folder nay co trong he thong chua
			// neu co roi thi chi cap nhat id
			MyFolder tontai = getDao().queryBuilder().where()
					.eq(MyFolder.ABSPATH_F, obj.getAbsPathForSQL()).queryForFirst();
			if (tontai == null) {
				// try to create FK First
				if (obj.getParentFolder() != null) {
					obj.getParentFolder().insert();
				}
				obj.load();
				return super.insert(obj);
			} else {
				obj.setId(tontai.getId());
				obj.reset();
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public ArrayList<MySong> getAllRecursiveSongs(MyFolder obj) {
		ArrayList<MySong> recursiveSongs = new ArrayList<MySong>();
		return _loadRecursiveSongs(obj, recursiveSongs);
	}

	private ArrayList<MySong> _loadRecursiveSongs(MyFolder root,
			ArrayList<MySong> recursiveSongs) {
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
		// set from SOURCE
		if (getSource() == MySource.DISK_SOURCE) {
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

					tmps = new MySong();
					tmps.setDao(getGlobalDAO().getMySongDAO());
					tmps.setPath(item.getAbsolutePath());
					childsSong.add(tmps);
				}
			}
		} else if (getSource() == MySource.DB_SOURCE) {
			try {
				// get folder from DB first
				MyFolder fd = getDao().queryBuilder().where()
						.eq(MyFolder.ABSPATH_F, obj.getAbsPathForSQL())
						.queryForFirst();
				if (fd == null) {
					return childsSong;
				}
				// get all Path belong to this
				List<MyPath> paths = getGlobalDAO().getMyPathDAO().getDao()
						.queryBuilder().where()
						.eq(MyPath.P_FOLDER_ID, fd.getId()).query();
				for (MyPath item : paths) {
					MySong song_tmp = getGlobalDAO().getMySongDAO().getDao()
							.queryBuilder().where()
							.eq(MySong.PATH_ID, item.getId()).queryForFirst();

					if (song_tmp != null) {
						song_tmp.setDao(getGlobalDAO().getMySongDAO());
						song_tmp.setLoaded(true);// importance
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

	@Override
	public void load(MyFolder obj) {
		if (getSource() == MySource.DISK_SOURCE) {
			obj.setFolderName(MyFileHelper.getFolderName(obj.getAbsPath()));
			obj.setLoaded(true);
		} else if (getSource() == MySource.DB_SOURCE) {
			super.load(obj);
		}
	}

	public ArrayList<MyFolder> getChildFolders(MyFolder obj) {
		ArrayList<MyFolder> re = new ArrayList<MyFolder>();
		if (getSource() == MySource.DISK_SOURCE) {
			File f = new File(obj.getAbsPath());
			File[] tmp = f.listFiles();
			// no childs
			if (tmp == null) {
				return re;
			}
			// has childs
			MyFolder tmp__;
			for (File item : tmp) {
				if (item.isDirectory()) {
					tmp__ = new MyFolder(item.getAbsolutePath());
					tmp__.setDao(this);// quan trong
					re.add(tmp__);
				}
			}
			return re;
		} else if (getSource() == MySource.DB_SOURCE) {
			try {
				List<MyFolder> fds = getDao().queryBuilder().where()
						.eq(MyFolder.PARENT_ID, obj.getId()).query();
				for (MyFolder item : fds) {
					item.setDao(this);
					re.add(item);
				}

				return re;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return re;
	}

	@Override
	public Boolean delete(MyFolder obj) {
		if (getSource() == MySource.DB_SOURCE) {
			for (MyFolder tmp : obj.getChildFolders()) {
				tmp.delete();
			}
			for (MySong tmp : obj.getChildSongs()) {
				tmp.delete();
			}
			return true;
		} else if (getSource() == MySource.DISK_SOURCE) {
			return MyFileHelper.delete(obj.getAbsPath());
		}
		return false;
	}

	public Boolean delete(MyFolder obj, Boolean removeFromDisk) {
		if (obj == null || obj.getAbsPath() == null) {
			return false;
		}
		if (removeFromDisk) {
			// neu obj da co san absPath va tu DISK
			if (getSource() == MySource.DISK_SOURCE && obj.getAbsPath() != null) {
				return delete(obj);
			}

			// Init new 2 layers delete script
			Integer bk = obj.getGlobalDAO().getSource();
			// try to switch to DB SOURCE first to get info
			obj.getGlobalDAO().setSource(MySource.DB_SOURCE);
			// call to load absPath in Path fisrt
			obj.getAbsPath();// trigger load absPath before delete from DB
			// call delete
			obj.delete();
			// absPath would still be existed after call above delete
			// change to DISK SOURCE
			obj.getGlobalDAO().setSource(MySource.DISK_SOURCE);
			obj.delete();
			// swicth to previous SOURCE
			obj.getGlobalDAO().setSource(bk);
			// finish
			return true;
		} else {
			return delete(obj);
		}
	}
}
