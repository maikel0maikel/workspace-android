package com.qdcatplayer.main.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyPathDAO;
import com.qdcatplayer.main.libraries.MyFileHelper;

@DatabaseTable(tableName = "MyPaths")
public class MyPath extends _MyEntityAbstract<MyPathDAO> {
	// because file/folder has no parent will got null too
	// so, we need to declare new Boolean varible to separate meaning
	// of 2 concept: "no parent" and "not ready yet"
	private Boolean _parentFolder_ready = false;
	@DatabaseField(unique = true, canBeNull = false)
	private String absPath = "";
	@DatabaseField
	protected String fileName = null;

	@DatabaseField(canBeNull = true, foreign = true)
	private MyFolder parentFolder = null;

	@DatabaseField(foreign = true, canBeNull = false)
	private MySong song = null;

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
		// pass custom DAO to MyFolder parent for later use
		// Ngay tai luc goi nay thi GlobalDAO se tao ra mot custom moi neu chua
		// co san
		parentFolder.setDao(getGlobalDAO().getMyFolderDAO());

		_parentFolder_ready = true;
		return parentFolder;
	}

	public MySong getSong() {
		return song;
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
		getSong();
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

	public void setSong(MySong song) {
		this.song = song;
	}

	@Override
	public Boolean update() {
		// TODO Auto-generated method stub
		return null;
	}

}
