package com.qdcatplayer.main.objects;

import java.io.File;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.libraries.MyFileHelper;

@DatabaseTable(tableName="MyFolders")
public class MyFolder extends MyPath {
	private ArrayList<MyFolder> _childsFolder = null;
	private ArrayList<MySong> _childsSong = null;
	private ArrayList<MySong> _recursiveSongs=null;
	public MyFolder() {
		// TODO Auto-generated constructor stub
		super();
	}
	public MyFolder(String absPath) {
		// TODO Auto-generated constructor stub
		super(absPath);
	}
	public ArrayList<MyFolder> getChildFolders()
	{
		if(_childsFolder!=null)
		{
			return _childsFolder;
		}
		//lazy loading
		_childsFolder = new ArrayList<MyFolder>();
		//do not support file/soundfile
		if(!super.isFolder())
		{
			return _childsFolder;
		}
		
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
		//do not support file/soundfile
		if(!super.isFolder())
		{
			return _childsSong;
		}
		
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
	@Override
	public Boolean reset()
	{
		super.reset();
		_childsFolder = null;
		_childsSong=null;
		return true;
	}
	public String getFolderName()
	{
		if(_fileName!=null)
		{
			return _fileName;
		}
		_fileName = MyFileHelper.getFolderName(getAbsPath());
		return _fileName;
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
}
