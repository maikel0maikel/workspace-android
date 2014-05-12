package com.qdcatplayer.main.Entities;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyAlbumDAO;
import com.qdcatplayer.main.Libraries.MyStringHelper;

@DatabaseTable(tableName = "MyAlbums")
public class MyAlbum extends _MyEntityAbstract<MyAlbumDAO, MyAlbum> {
	/**
	 * 
	 */
	public static final String NAME_F = "name";

	private Bitmap cover = null;
	@DatabaseField(unique = true)
	private String name = null;

	private ArrayList<MySong> songs = null;

	public MyAlbum() {

	}

	public MyAlbum(String name) {
		setName(name);
	}

	public Bitmap getCover() {
		return cover;
	}

	public String getName() {
		// de phong truong hop setName bang tay
		if (name == null) {
			super.load();
		}
		return MyStringHelper.filterNullOrBlank(name, UNKNOWN_VALUE);
	}

	/**
	 * Khong ho tro DISK SOURCE vi ly do Disk khong to chuc theo CSDL
	 * 
	 * @return
	 */
	public ArrayList<MySong> getSongs() {
		if (songs == null) {
			songs = getDao().getSongs(this);
		}
		return songs;
	}

	@Override
	public void reset() {
		super.reset();
		cover = null;
		name = null;
		songs = null;
	}

	public void setCover(Bitmap cover) {
		this.cover = cover;
	}

	public void setName(String name_) {
		this.name = MyStringHelper.filterSQLSpecial(name_, UNKNOWN_VALUE);
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

}
