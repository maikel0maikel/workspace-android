package com.qdcatplayer.objects;

public class MyBitrate {
	private Long _value=0l;
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
}
