package com.qdcatplayer.main.Entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyArtistDAO;

@DatabaseTable(tableName = "MyArtists")
public class MyArtist extends _MyEntityAbstract<MyArtistDAO, MyArtist> {
	public static final String NAME_F = "name";

	@ForeignCollectionField
	private ForeignCollection<MySong> songs = null;

	@DatabaseField(unique = true)
	private String name = "";// never null

	public MyArtist() {

	}

	public MyArtist(String name) {
		setName(name);
	}

	public ForeignCollection<MySong> getSongs() {
		super.load();
		return songs;
	}

	public String getName() {
		return name;
	}


	@Override
	public void reset() {
		super.reset();
		name = null;
	}

	public void setMySongs(ForeignCollection<MySong> mySongs) {
		this.songs = mySongs;
	}

	public void setName(String name_) {
		name = name_ == null ? "" : name_;
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
