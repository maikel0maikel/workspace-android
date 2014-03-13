package com.qdcatplayer.main.objects;

import java.io.File;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyPathDAO;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.libraries.MyFileHelper;

@DatabaseTable(tableName="MyPaths")
public class MyPath extends _MyObjectAbstract<MyPathDAO> {
	@DatabaseField(unique=true, canBeNull=false)
	private String _absPath = "";
	@DatabaseField
	protected String _fileName = null;
	@DatabaseField(foreign=true, canBeNull=false)
	private MySong _song = null;
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
	@Override
	public Boolean reset()
	{
		//clear all reference members
		_parentFolder = null;
		_fileName=null;
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
	//qd FAIL
	public String getFileExtension(Boolean withDot)
	{
		if(getAbsPath()==null || getAbsPath().equals(""))
		{
			return "";
		}
		return MyFileHelper.getFileExtension(getAbsPath(), withDot);
	}
	public String getAbsPath()
	{
		return _absPath;
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
