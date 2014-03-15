package com.qdcatplayer.main.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyAlbumDAO;

import android.graphics.Bitmap;

@DatabaseTable(tableName="MyAlbums")
public class MyAlbum extends _MyEntityAbstract<MyAlbumDAO> {
	@DatabaseField(unique=true)
	private String name="";//never null
	@ForeignCollectionField
	private ForeignCollection<MySong> songs = null;
	
	private Bitmap cover=null;
	public MyAlbum() {
		
	}
	public MyAlbum(String name) {
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
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id_)
	{
		id = id_==null?0:id_;
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
		return getDao().insert(this);
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
