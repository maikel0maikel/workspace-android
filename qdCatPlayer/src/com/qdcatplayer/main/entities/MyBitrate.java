package com.qdcatplayer.main.Entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyBitrateDAO;

@DatabaseTable(tableName="MyBitrates")
public class MyBitrate extends _MyEntityAbstract<MyBitrateDAO, MyBitrate> {
	public static final String VALUE_F = "value";
	@ForeignCollectionField
	private ForeignCollection<MySong> songs = null;
	@DatabaseField(unique=true, canBeNull=false)
	private Long value=0l;//never null
	public MyBitrate() {
		
	}
	public MyBitrate(Long value_) {
		// TODO Auto-generated constructor stub
		value = value_;
		if(value==null)
		{
			value=0l;
		}
	}
	public MyBitrate(String value_) {
		// TODO Auto-generated constructor stub
		if(value_==null)
		{
			value=0l;
		}
		try{
			value = Long.parseLong(value_);
		}catch(NumberFormatException ex)
		{
			ex.printStackTrace();
			value = 0l;
		}
	}

	public ForeignCollection<MySong> getMySongs() {
		return songs;
	}
	public Long getValue()
	{
		return value;
	}

	@Override
	public void reset() {
		value = null;
		songs = null;
		super.reset();
	}
	public void setMySongs(ForeignCollection<MySong> mySongs) {
		this.songs = mySongs;
	}
	public Long setValue()
	{
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	@Override
	public Integer insert() {
		//very importance
		//since MyBitrate may be loaded when fetching MySong
		//if not force to set loaded=true then
		//new load script will be acted and reset will be called
		//then all data pre-loaded will swiped out
		setLoaded(true);
		return super.insert();
	}

}
