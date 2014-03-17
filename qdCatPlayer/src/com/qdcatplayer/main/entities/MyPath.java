package com.qdcatplayer.main.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyPathDAO;
import com.qdcatplayer.main.libraries.MyFileHelper;

@DatabaseTable(tableName = "MyPaths")
public class MyPath extends _MyEntityAbstract<MyPathDAO> {
	public static final String ABSPATH_F = "absPath";
	public static final String P_FOLDER_ID = "parentFolder_id";
	// because file/folder has no parent will got null too
	// so, we need to declare new Boolean varible to separate meaning
	// of 2 concept: "no parent" and "not ready yet"
	private Boolean _parentFolder_ready = false;
	@DatabaseField(unique = true, canBeNull = false)
	private String absPath = null;
	@DatabaseField(canBeNull=false)
	protected String fileName = null;

	@DatabaseField(canBeNull = true, foreign = true)
	private MyFolder parentFolder = null;

	/**
	 * Khi insert Path thi do Song van chua duoc insert
	 * nen tam thoi song=null, sau khi MyPath duoc insert thanh cong thi
	 * Song se co nhiem vu gan nguoc id cua minh qua chp Path.Song
	 
		@DatabaseField(foreign = true, canBeNull = true)
		private MySong song = null;
	*/

	public MyPath() {
		// TODO Auto-generated constructor stub
	}

	public MyPath(String absPath) {
		// TODO Auto-generated constructor stub
		setAbsPath(absPath);
	}

	@Override
	public Integer delete() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAbsPath() {
		if(absPath==null)
		{
			absPath = getDao().getAbsPath(this);
		}
		return absPath;
	}

	// qd FAIL
	public String getFileExtension(Boolean withDot) {
		if (getAbsPath() == null || getAbsPath().equals("")) {
			return "";
		}
		return MyFileHelper.getFileExtension(getAbsPath(), withDot);
	}

	public String getFileName() {
		if (fileName != null) {
			return fileName;
		}
		fileName = MyFileHelper.getFileName(absPath, false);
		return fileName;
	}

	public MyFolder getParentFolder() {
		if (_parentFolder_ready == true) {
			return parentFolder;
		}
		// do not know DAO !
		if (getDao() == null) {
			return null;
		}
		// get through DAO is recommended, DAO will look SOURCE to choose
		// where to get Parent Obj
		parentFolder = getDao().getParentFolder(this);
		//DAO was already set by above call
		_parentFolder_ready = true;
		return parentFolder;
	}

	@Override
	public Integer insert() {
		return getDao().insert(this);
	}
	@Override
	public Boolean loadAllProperties() {
		// TODO Auto-generated method stub
		getParentFolder();
		getAbsPath();
		getFileName();
		return true;
	}

	@Override
	public Boolean reset() {
		// clear all reference members
		parentFolder = null;
		fileName = null;
		// set lazy state
		_parentFolder_ready = false;
		return true;
	}

	public Boolean setAbsPath(String path) {
		absPath = path;
		return true;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setParentFolder(MyFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	@Override
	public Boolean update() {
		// TODO Auto-generated method stub
		return null;
	}

}
