package com.qdcatplayer.main.entities;

import java.io.File;
import java.util.ArrayList;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MyPathDAO;
import com.qdcatplayer.main.libraries.MyFileHelper;

@DatabaseTable(tableName = "MyFolders")
public class MyFolder extends _MyEntityAbstract<MyFolderDAO> {
	public static final String ABSPATH_F = "absPath";
	@DatabaseField(unique = true, canBeNull = false)
	private String absPath = null;
	private ArrayList<MyFolder> childsFolder = null;

	private ArrayList<MySong> childsSong = null;

	@DatabaseField(canBeNull = true)
	private String folderName = null;
	@DatabaseField(canBeNull = true, foreign = true)
	private MyFolder parentFolder = null;

	// because file/folder has no parent will got null too
	// so, we need to declare new Boolean varible to separate meaning
	// of 2 concept: "no parent" and "not ready yet"
	private Boolean parentFolder_ready = false;
	@ForeignCollectionField
	private ForeignCollection<MyPath> paths = null;

	public MyFolder() {

	}
	public MyFolder(String absPath) {
		// TODO Auto-generated constructor stub
		setAbsPath(absPath);
	}

	@Override
	public Integer delete() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAbsPath() {
		return absPath;
	}

	/**
	 * Lay tat ca SONG de quy ke tu root
	 * 
	 * @return
	 */
	public ArrayList<MySong> getAllRecursiveSongs() {
		return getDao().getAllRecursiveSongs(this);
	}

	public ArrayList<MyFolder> getChildFolders() {
		if (childsFolder != null) {
			return childsFolder;
		}
		// lazy loading
		childsFolder = new ArrayList<MyFolder>();

		File f = new File(getAbsPath());
		File[] tmp = f.listFiles();
		// no childs
		if (tmp == null) {
			return childsFolder;
		}
		// has childs
		for (File item : tmp) {
			if (item.isDirectory()) {
				MyFolder tmp__ = new MyFolder(item.getAbsolutePath());
				tmp__.setDao(getDao());//quan trong
				childsFolder.add(tmp__);
			}
		}
		return childsFolder;
	}

	/**
	 * 
	 * @return Khong bao gio return null, neu khong co thi return mang rong
	 */
	public ArrayList<MySong> getChildSongs() {
		return getDao().getChildSongs(this);
	}

	public String getFolderName() {
		if (folderName != null) {
			return folderName;
		}
		folderName = MyFileHelper.getFolderName(getAbsPath());
		return folderName;
	}

	public MyFolder getParentFolder() {
		if (parentFolder_ready == true) {
			return parentFolder;
		}
		// do not know DAO !
		if (getDao() == null) {
			return null;
		}
		// get through DAO is recommended, DAO will look SOURCE to choose
		// where to get Parent Obj
		parentFolder = getDao().getParentFolder(this);
		parentFolder_ready = true;
		//set dao
		if(parentFolder != null){
			parentFolder.setDao(getGlobalDAO().getMyFolderDAO());
		}
		return parentFolder;
	}

	public ForeignCollection<MyPath> getPaths() {
		return paths;
	}

	@Override
	public Integer insert() {
		// TODO Auto-generated method stub
		return getDao().insert(this);
	}

	@Override
	public Boolean loadAllProperties() {
		getFolderName();
		getAbsPath();
		getParentFolder();
		return true;
	}

	@Override
	public Boolean reset() {
		// clear all reference members
		parentFolder = null;
		folderName = null;
		// set lazy state
		parentFolder_ready = false;
		return true;
	}

	public void setAbsPath(String absPath_) {
		absPath = absPath_;
	}

	public void setFolderName(String folderName_) {
		folderName = folderName_;
	}

	public void setParentFolder(MyFolder parentFolder_) {
		parentFolder = parentFolder_;
	}

	public void setPaths(ForeignCollection<MyPath> paths) {
		this.paths = paths;
	}

	@Override
	public Boolean update() {
		// TODO Auto-generated method stub
		return null;
	}

}
