package com.qdcatplayer.main.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyBitrateDAO;

@DatabaseTable(tableName="MyBitrates")
public class MyBitrate extends _MyEntityAbstract<MyBitrateDAO, MyBitrate> {
	public static final String VALUE_F = "value";
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
	public Long getValue()
	{
		return value;
	}

	@Override
	public void reset() {
		value = null;
		super.reset();
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
