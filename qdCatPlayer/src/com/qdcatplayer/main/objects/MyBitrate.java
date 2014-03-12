package com.qdcatplayer.main.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MyBitrates")
public class MyBitrate {
	@DatabaseField(generatedId=true)
	private Integer _id=null;
	@DatabaseField(unique=true, canBeNull=false)
	private Long _value=0l;//never null
	
	public MyBitrate() {
		
	}
	public MyBitrate(Long value) {
		// TODO Auto-generated constructor stub
		_value = value;
		if(_value==null)
		{
			_value=0l;
		}
	}
	public MyBitrate(String value) {
		// TODO Auto-generated constructor stub
		if(value==null)
		{
			_value=0l;
		}
		try{
			_value = Long.parseLong(value);
		}catch(NumberFormatException ex)
		{
			ex.printStackTrace();
			_value = 0l;
		}
	}
	public Long getValue()
	{
		return _value;
	}
	public Long setValue()
	{
		return _value;
	}
}
