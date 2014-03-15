package com.qdcatplayer.main.entities;

import java.io.File;
import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs.MyPathDAO;
import com.qdcatplayer.main.DAOs.MySongDAO;
import com.qdcatplayer.main.libraries.MyFileHelper;

@DatabaseTable(tableName="MyPaths")
public class MyPath extends _MyEntityAbstract<MyPathDAO> {
	@DatabaseField(unique=true, canBeNull=false)
	private String absPath = "";
	@DatabaseField
	protected String fileName = null;
	@DatabaseField(foreign=true, canBeNull=false)
	private MySong song = null;
	@DatabaseField(canBeNull=true, foreign=true)
	private MyFolder parentFolder = null;
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
		absPath = path;
		return true;
	}
	public MyFolder getParentFolder()
	{
		if(_parentFolder_ready==true)
		{
			return parentFolder;
		}
		//do not know DAO !
		if(getDao()==null)
		{
			return null;
		}
		//get through DAO is recommended, DAO will look SOURCE to choose
		//where to get Parent Obj
		parentFolder = getDao().getParentFolder(this);
		//pass custom DAO to MyFolder parent for later use
		//Ngay tai luc goi nay thi GlobalDAO se tao ra mot custom moi neu chua co san
		parentFolder.setDao(getGlobalDAO().getMyFolderDAO());
		parentFolder.setDao(getGlobalDAO().getMyFolderDAO());
		
		_parentFolder_ready=true;
		return parentFolder;
	}
	@Override
	public Boolean reset()
	{
		//clear all reference members
		parentFolder = null;
		fileName=null;
		//set lazy state
		_parentFolder_ready=false;
		return true;
	}
	public String getFileName()
	{
		if(fileName!=null)
		{
			return fileName;
		}
		fileName = MyFileHelper.getFileName(absPath, false);
		return fileName;
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
		return absPath;
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
