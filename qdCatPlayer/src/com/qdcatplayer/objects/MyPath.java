package com.qdcatplayer.objects;

import java.io.File;
import java.util.ArrayList;
import com.qdcatplayer.libraries.MyFileHelper;

public class MyPath {
	private String _absPath = "";
	private String _fileName = null;
	private Boolean _isSoundFile = null;
	private Boolean _isFile = null;
	private Boolean _isFolder = null;
	private MyPath _parentFolder = null;
		//because file/folder has no parent will got null too
		//so, we need to declare new Boolean varible to separate meaning
		//of 2 concept: "no parent" and "not ready yet"
		private Boolean _parentFolder_ready = false;
	private ArrayList<MyPath> _childsFolder = null;
	public MyPath() {
		// TODO Auto-generated constructor stub
	}
	public MyPath(String absPath) {
		// TODO Auto-generated constructor stub
		setAbsPath(absPath);
	}
	public Boolean setAbsPath(String path)
	{
		_absPath = path;
		return true;
	}
	public Boolean isSoundFile()
	{
		if(_isSoundFile!=null)
		{
			return _isSoundFile;
		}
		_isSoundFile = MyFileHelper.isSoundFile(_absPath);
		return _isSoundFile;
	}
	public Boolean isFile()
	{
		if(_isFile!=null)
		{
			return _isFile;
		}
		if(isSoundFile()==true)
		{
			_isFile = true;
			return _isFile;
		}
		File f=new File(_absPath);
		
		_isFile = f.isFile();
		return _isFile;
	}
	public Boolean isFolder()
	{
		if(_isFolder!=null)
		{
			return _isFolder;
		}
		File f=new File(_absPath);
		
		_isFolder = f.isDirectory();
		return _isFolder;
	}
	public ArrayList<MyPath> getChildFolders()
	{
		if(_childsFolder!=null)
		{
			return _childsFolder;
		}
		//lazy loading
		_childsFolder = new ArrayList<MyPath>();
		File f = new File(_absPath);
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
						new MyPath(
								item.getAbsolutePath()
								)
						);
			}
		}
		return _childsFolder;
	}
	public MyPath getParentFolder()
	{
		if(_parentFolder_ready==true)
		{
			return _parentFolder;
		}
		File f = new File(_absPath);
		String parent = f.getParent();
		//no parent
		if(parent==null)
		{
			_parentFolder = null;
			return _parentFolder;
		}
		//has parent
		_parentFolder = new MyPath(
				f.getParent()
				);
		_parentFolder_ready=true;
		return _parentFolder;
	}
	public Boolean update()
	{
		//clear all reference members
		_childsFolder = null;
		_parentFolder = null;
		_isFile = null;
		//set lazy state
		_parentFolder_ready=false;
		return true;
	}
	public String getFileName()
	{
		if(_fileName!=null)
		{
			return _fileName;
		}
		_fileName = MyFileHelper.getFileName(_absPath, false);
		return _fileName;
	}
	public String getFileExtension()
	{
		if(getFileName()==null)
		{
			return null;
		}
		
		return MyFileHelper.getFileExtension(getFileName(), false);
	}
	public String getAbsPath()
	{
		return _absPath;
	}
}
