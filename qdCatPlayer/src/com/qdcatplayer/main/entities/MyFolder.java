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

@DatabaseTable(tableName="MyFolders")
public class MyFolder extends _MyEntityAbstract<MyFolderDAO> {
	@DatabaseField(unique=true, canBeNull=false)
	private String _absPath = "";
	@DatabaseField
	private String _folderName = null;
	@ForeignCollectionField
	private ForeignCollection<MyPath> _paths = null;
	
	@DatabaseField(canBeNull=true, foreign=true)
	private MyFolder _parentFolder = null;
		//because file/folder has no parent will got null too
		//so, we need to declare new Boolean varible to separate meaning
		//of 2 concept: "no parent" and "not ready yet"
		private Boolean _parentFolder_ready = false;
		
	private ArrayList<MyFolder> _childsFolder = null;
	private ArrayList<MySong> _childsSong = null;
	private ArrayList<MySong> _recursiveSongs=null;
	public MyFolder() {
		
	}
	public MyFolder(String absPath) {
		// TODO Auto-generated constructor stub
		setAbsPath(absPath);
	}
	public Boolean setAbsPath(String path) {
		_absPath = path;
		return true;
	}
	public ArrayList<MyFolder> getChildFolders()
	{
		if(_childsFolder!=null)
		{
			return _childsFolder;
		}
		//lazy loading
		_childsFolder = new ArrayList<MyFolder>();

		File f = new File(getAbsPath());
		File[] tmp = f.listFiles();
		//no childs
		if(tmp==null)
		{
			return _childsFolder;
		}
		//has childs
		for(File item:tmp)
		{
			if(item.isDirectory())
			{
				_childsFolder.add(
						new MyFolder(
								item.getAbsolutePath()
								)
						);
			}
		}
		return _childsFolder;
	}
	public MyFolder getParentFolder()
	{
		if(_parentFolder_ready==true)
		{
			return _parentFolder;
		}
		//do not know DAO !
		if(getDao()==null)
		{
			return null;
		}
		//get through DAO is recommended, DAO will look SOURCE to choose
		//where to get Parent Obj
		_parentFolder = getDao().getParentFolder(this);
		_parentFolder_ready=true;
		return _parentFolder;
	}
	/**
	 * 
	 * @return Khong bao gio return null, neu khong co thi return mang rong
	 */
	public ArrayList<MySong> getChildSongs()
	{
		if(_childsSong!=null)
		{
			return _childsSong;
		}
		//lazy loading
		_childsSong = new ArrayList<MySong>();
		
		File f = new File(getAbsPath());
		File[] tmp = f.listFiles();
		//no childs
		if(tmp==null)
		{
			return _childsSong;
		}
		//has childs
		MySong tmps;
		for(File item:tmp)
		{
			if(item.isFile() && MyFileHelper.isSoundFile(item.getAbsolutePath()))
			{
				tmps=new MySong(item.getAbsolutePath());
				_childsSong.add(tmps);
			}
		}
		return _childsSong;
	}
	public String getAbsPath()
	{
		return _absPath;
	}
	@Override
	public Boolean reset()
	{
		//clear all reference members
		_parentFolder = null;
		_folderName=null;
		//set lazy state
		_parentFolder_ready=false;
		return true;
	}
	public String getFolderName()
	{
		if(_folderName!=null)
		{
			return _folderName;
		}
		_folderName = MyFileHelper.getFolderName(getAbsPath());
		return _folderName;
	}
	/**
	 * Lay tat ca SONG de quy ke tu root
	 * @return
	 */
	public ArrayList<MySong> getAllRecursiveSongs()
	{
		if(_recursiveSongs!=null)
		{
			return _recursiveSongs;
		}
		//init new array
		_recursiveSongs=new ArrayList<MySong>();
		//call recursive method
		_loadRecursiveSongs(this);
		return _recursiveSongs;
	}
	private void _loadRecursiveSongs(MyFolder root)
	{
		if(root==null)
		{
			return;
		}
		//add direct first
		for(MySong item:root.getChildSongs())
		{
			_recursiveSongs.add(item);
		}
		//finish on this node
		if(root.getChildFolders().size()<=0)
		{
			return;
		}
		for(MyFolder item:root.getChildFolders())
		{
			_loadRecursiveSongs(item);
		}
	}
	@Override
	public Boolean loadAllProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer insert() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean update() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer delete() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
