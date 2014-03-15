package com.qdcatplayer.main.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyBitrateDAO;

@DatabaseTable(tableName="MyBitrates")
public class MyBitrate extends _MyEntityAbstract<MyBitrateDAO> {
	@DatabaseField(unique=true, canBeNull=false)
	private Long value=0l;//never null
	@ForeignCollectionField
	private ForeignCollection<MySong> mySongs = null;
	
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
	public Long getValue()
	{
		return value;
	}
	public Long setValue()
	{
		return value;
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