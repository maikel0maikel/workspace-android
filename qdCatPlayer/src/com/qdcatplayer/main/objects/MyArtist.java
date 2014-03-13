package com.qdcatplayer.main.objects;

import android.graphics.Bitmap;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MyArtists")
public class MyArtist {
	@DatabaseField(generatedId=true)
	private Integer _id=null;
	@DatabaseField(unique=true)
	private String _name="";//never null
	@ForeignCollectionField
	private ForeignCollection<MySong> _mySongs = null;
	public MyArtist() {
		
	}
	public MyArtist(String name) {
		setName(name);
	}
	public String getName()
	{
		return _name;
	}
	public Boolean setName(String name)
	{
		_name=name==null?"":name;
		return true;
	}
	public Integer getId()
	{
		return _id;
	}
	public Boolean setId(Integer id)
	{
		_id = id==null?0:id;
		return true;
	}
}
