package com.qdcatplayer.objects;

public class MyFormat {
	private String _extension = "";
	private String _mimetype= "";
	public MyFormat() {
		// TODO Auto-generated constructor stub
	}
	public MyFormat(String extension) {
		// TODO Auto-generated constructor stub
		this._extension = extension;
	}
	public String getExtension()
	{
		return _extension;
	}
}
