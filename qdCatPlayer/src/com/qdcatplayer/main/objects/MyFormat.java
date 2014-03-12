package com.qdcatplayer.main.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="MyFormats")
public class MyFormat {
	@DatabaseField(generatedId=true)
	private Integer _id=null;
	@DatabaseField(unique=true, canBeNull=false)
	private String _extension = null;//never null
	@DatabaseField(canBeNull=false)
	private String _mimetype= "audio/mp3";
	
	public MyFormat() {
	}
	public MyFormat(String extension) {
		setExtension(extension);
	}
	public String getExtension()
	{
		return _extension;
	}
	public Boolean setExtension(String extension)
	{
		this._extension = extension==null?"":extension;
		return true;
	}
}
