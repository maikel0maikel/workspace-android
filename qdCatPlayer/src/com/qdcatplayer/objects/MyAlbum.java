package com.qdcatplayer.objects;

import android.graphics.Bitmap;

public class MyAlbum {
	private String _name="";//never null
	private Bitmap _cover=null;
	public MyAlbum(String name) {
		// TODO Auto-generated constructor stub
		_name=name==null?"":name;
	}
	public String getName()
	{
		return _name;
	}
}
