package com.qdcatplayer.main.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyArtistDAO;

@DatabaseTable(tableName = "MyArtists")
public class MyArtist extends _MyEntityAbstract<MyArtistDAO, MyArtist> {
	public static final String NAME_F = "name";

	@ForeignCollectionField
	private ForeignCollection<MySong> mySongs = null;

	@DatabaseField(unique = true)
	private String name = "";// never null

	public MyArtist() {

	}

	public MyArtist(String name) {
		setName(name);
	}

	@Override
	public Boolean delete() {
		// TODO Auto-generated method stub
		return null;
	}

	public ForeignCollection<MySong> getMySongs() {
		return mySongs;
	}

	public String getName() {
		return name;
	}

	@Override
	public Integer insert() {
		// TODO Auto-generated method stub
		return getDao().insert(this);
	}


	@Override
	public void reset() {

	}

	public void setMySongs(ForeignCollection<MySong> mySongs) {
		this.mySongs = mySongs;
	}

	public void setName(String name_) {
		name = name_ == null ? "" : name_;
	}

	@Override
	public Boolean update() {
		// TODO Auto-generated method stub
		return null;
	}
}
