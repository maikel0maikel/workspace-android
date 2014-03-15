package com.qdcatplayer.main.entities;

import android.graphics.Bitmap;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyArtistDAO;

@DatabaseTable(tableName="MyArtists")
public class MyArtist extends _MyEntityAbstract<MyArtistDAO> {
	@DatabaseField(unique=true)
	private String name="";//never null
	@ForeignCollectionField
	private ForeignCollection<MySong> mySongs = null;
	public MyArtist() {
		
	}
	public MyArtist(String name) {
		setName(name);
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name_)
	{
		name=name_==null?"":name_;
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
