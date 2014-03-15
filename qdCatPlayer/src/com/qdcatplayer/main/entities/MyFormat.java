package com.qdcatplayer.main.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyFormatDAO;

@DatabaseTable(tableName="MyFormats")
public class MyFormat extends _MyEntityAbstract<MyFormatDAO> {
	@DatabaseField(unique=true, canBeNull=false)
	private String extension = null;//never null
	@DatabaseField(canBeNull=false)
	private String mimeType= "audio/mp3";
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	@ForeignCollectionField
	private ForeignCollection<MySong> mySongs = null;
	
	public MyFormat() {
	}
	public MyFormat(String extension) {
		setExtension(extension);
	}
	public String getExtension()
	{
		return extension;
	}
	public void setExtension(String extension)
	{
		this.extension = extension==null?"":extension;
	}
	@Override
	public Boolean loadAllProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean reset() {
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
