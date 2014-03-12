package com.qdcatplayer.main.objects;

import java.io.File;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.libraries.MyFileHelper;

@DatabaseTable(tableName="MyPaths")
public class MyPath {
	@DatabaseField(generatedId = true)
	private Integer _id = null;
	@DatabaseField(unique=true, canBeNull=false)
	private String _absPath = "";
	@DatabaseField
	protected String _fileName = null;
	@DatabaseField
	private Boolean _isSoundFile = null;
	@DatabaseField
	private Boolean _isFile = null;
	@DatabaseField
	private Boolean _isFolder = null;
	@DatabaseField(canBeNull=true, foreign=true)
	private MyFolder _parentFolder = null;
		//because file/folder has no parent will got null too
		//so, we need to declare new Boolean varible to separate meaning
		//of 2 concept: "no parent" and "not ready yet"
		private Boolean _parentFolder_ready = false;
	
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
		//
		if(!isFile())
		{
			_isSoundFile=false;
		}
		else
		{
			_isSoundFile = MyFileHelper.isSoundFile(_absPath);
		}
		if(_isSoundFile)
		{
			_isFile = true;
			_isFolder = false;
		}
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
		//get for other too
		_isFolder = !_isFile;
		return _isFile;
	}
	public Boolean isFolder()
	{
		if(_isFolder!=null)
		{
			return _isFolder;
		}
		File f=new File(_absPath);
		
		_isFolder = f.isDirectory();//FAIL
		//get for other too
		_isFile = !_isFolder;
		_isSoundFile=!_isFolder;
		return _isFolder;
	}
	
	public MyFolder getParentFolder()
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
		_parentFolder = new MyFolder(
				parent+"/"
				);
		_parentFolder_ready=true;
		return _parentFolder;
	}
	public Boolean reset()
	{
		//clear all reference members
		_parentFolder = null;
		_isFile = null;
		_isFolder = null;
		_isSoundFile = null;
		_fileName=null;
		//set lazy state
		_parentFolder_ready=false;
		return true;
	}
	public String getFileName()
	{
		//do not support folder
		if(isFolder()==true)
		{
			return "";
		}
		if(_fileName!=null)
		{
			return _fileName;
		}
		_fileName = MyFileHelper.getFileName(_absPath, false);
		return _fileName;
	}
	public String getFileExtension()
	{
		//do not support folder
		if(isFolder()==true)
		{
			return "";
		}
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
