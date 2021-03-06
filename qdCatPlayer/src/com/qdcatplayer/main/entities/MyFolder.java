package com.qdcatplayer.main.Entities;

import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyFolderDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.Libraries.MyFileHelper;
import com.qdcatplayer.main.Libraries.MyStringHelper;

@DatabaseTable(tableName = "MyFolders")
public class MyFolder extends _MyEntityAbstract<MyFolderDAO, MyFolder> {
	public static final String ABSPATH_F = "absPath";
	public static String PARENT_ID = "parentFolder_id";
	@DatabaseField(unique = true, canBeNull = false)
	private String absPath = null;
	private ArrayList<MyFolder> childsFolder = null;

	private ArrayList<MySong> childsSong = null;

	@DatabaseField(canBeNull = true)
	private String folderName = null;
	@DatabaseField(canBeNull = true)
	private String trackingHash = null;
	@DatabaseField(canBeNull = true, foreign = true)
	private MyFolder parentFolder = null;

	// because file/folder has no parent will got null too
	// so, we need to declare new Boolean varible to separate meaning
	// of 2 concept: "no parent" and "not ready yet"
	private Boolean parentFolder_ready = false;

	private ArrayList<MySong> allRecursiveSongs = null;

	public MyFolder() {

	}

	public MyFolder(String absPath_) {
		// TODO Auto-generated constructor stub
		setAbsPath(absPath_);
	}

	public String getAbsPath() {
		if (getGlobalDAO().getSource() != MySource.DISK_SOURCE) {
			super.load();
		}
		return MyStringHelper.filterSQLSpecialAbsPath(absPath,UNKNOWN_VALUE, false);
	}
	public String getAbsPathForSQL() {
		if (getGlobalDAO().getSource() != MySource.DISK_SOURCE) {
			super.load();
		}
		return MyStringHelper.filterNullOrBlank(absPath,UNKNOWN_VALUE);
	}

	/**
	 * Lay tat ca SONG de quy ke tu root Ho tro cached
	 * 
	 * @return
	 */
	public ArrayList<MySong> getAllRecursiveSongs() {
		if (allRecursiveSongs == null) {
			allRecursiveSongs = getDao().getAllRecursiveSongs(this);
		}
		return allRecursiveSongs;
	}

	public ArrayList<MyFolder> getChildFolders() {
		if (childsFolder != null) {
			return childsFolder;
		}
		// lazy loading
		childsFolder = getDao().getChildFolders(this);
		return childsFolder;
	}

	/**
	 * 
	 * @return Khong bao gio return null, neu khong co thi return mang rong
	 */
	public ArrayList<MySong> getChildSongs() {
		if (childsSong == null) {
			childsSong = getDao().getChildSongs(this);
		}
		return childsSong;
	}

	public String getFolderName() {
		super.load();
		if(absPath.equals("/"))
		{
			return "[root]";
		}
		return MyStringHelper.filterNullOrBlank(folderName, UNKNOWN_VALUE);
	}

	public MyFolder getParentFolder() {
		// parent folder co bien Boolean rieng de kiem soat
		// khong dung chung loaded vi ly do lazy loading
		if (parentFolder_ready == true || parentFolder != null) {
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
		// set dao
		if (parentFolder != null) {
			parentFolder.setDao(getGlobalDAO().getMyFolderDAO());
		}
		return parentFolder;
	}

	@Override
	public void reset() {
		super.reset();
		// clear all reference members
		parentFolder = null;
		parentFolder_ready = false;
		folderName = null;
		childsSong = null;
		allRecursiveSongs = null;
		// do not reset absPath
	}

	public void setAbsPath(String absPath_) {
		absPath = MyStringHelper.filterSQLSpecialAbsPath(absPath_,UNKNOWN_VALUE, true);
	}

	public void setFolderName(String folderName_) {
		this.folderName = MyStringHelper.filterSQLSpecial(folderName_,
				UNKNOWN_VALUE);
	}

	public void setParentFolder(MyFolder parentFolder_) {
		parentFolder = parentFolder_;
	}

	public Boolean isOnDisk() {
		return MyFileHelper.isExist(getAbsPath());
	}

	public Boolean delete(Boolean removeFromDisk) {
		return getDao().delete(this, removeFromDisk);
	}

	public Integer getLevel() {
		return getAbsPath().split("/").length;
	}

	public String getLevelName(String delimiter, Integer minus_prefix) {
		String re = "";
		Integer level = getLevel() - minus_prefix;
		if (level < 0)
			level = 0;
		for (int i = 0; i < level; i++) {
			re += delimiter;
		}
		return re + getFolderName();
	}

	public Boolean isSubFolderOf(MyFolder ancestor) {
		// parent always has higher level
		if (getLevel() <= ancestor.getLevel()) {
			return false;
		}
		MyFolder parent_mp = getParentFolder();
		while (parent_mp != null) {
			if (parent_mp.getAbsPath().equals(ancestor.getAbsPath())) {
				return true;
			}
			parent_mp = parent_mp.getParentFolder();
		}
		return false;
	}
}
