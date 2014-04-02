package com.qdcatplayer.main.Entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyFormatDAO;

@DatabaseTable(tableName = "MyFormats")
public class MyFormat extends _MyEntityAbstract<MyFormatDAO, MyFormat> {
	public static final String EXTENSION_F = "extension";

	@DatabaseField(unique = true, canBeNull = false)
	private String extension = null;// never null

	@DatabaseField(canBeNull = false)
	private String mimeType = "audio/mp3";

	@ForeignCollectionField
	private ForeignCollection<MySong> songs = null;

	public MyFormat() {
	}

	public MyFormat(String extension) {
		setExtension(extension);
	}


	public String getExtension() {
		super.load();
		return extension;
	}

	public String getMimeType() {
		super.load();
		return mimeType;
	}

	public ForeignCollection<MySong> getMySongs() {
		super.load();
		return songs;
	}


	@Override
	public void reset() {
		super.reset();
		
		extension = null;
		mimeType = null;
		songs = null;
		
	}

	public void setExtension(String extension) {
		this.extension = extension == null ? "" : extension;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setMySongs(ForeignCollection<MySong> mySongs) {
		this.songs = mySongs;
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
