package com.qdcatplayer.main.Entities;

import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyArtistDAO;
import com.qdcatplayer.main.Libraries.MyStringHelper;

@DatabaseTable(tableName = "MyArtists")
public class MyArtist extends _MyEntityAbstract<MyArtistDAO, MyArtist> {
	public static final String NAME_F = "name";

	@DatabaseField(unique = true, canBeNull = false)
	private String name = null;

	/**
	 * KHÔNG dùng ForeignCollection, vì một vài lý do như setDao setLoaded
	 */
	private ArrayList<MySong> songs = null;

	public MyArtist() {

	}

	public MyArtist(String name) {
		setName(name);
	}

	public String getName() {
		// de phong truong hop setName bang tay
		if (name == null) {
			super.load();
		}
		return MyStringHelper.filterNullOrBlank(name, UNKNOWN_VALUE);
	}

	public ArrayList<MySong> getSongs() {
		if (songs == null) {
			songs = getDao().getSongs(this);
		}
		return songs;
	}

	@Override
	public Integer insert() {
		// very importance
		// since MyBitrate may be loaded when fetching MySong
		// if not force to set loaded=true then
		// new load script will be acted and reset will be called
		// then all data pre-loaded will swiped out
		setLoaded(true);
		return super.insert();
	}

	@Override
	public void reset() {
		super.reset();
		name = null;
	}

	public void setName(String name_) {
		this.name = MyStringHelper.filterSQLSpecial(name_, UNKNOWN_VALUE);
	}

}
