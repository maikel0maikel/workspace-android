package com.qdcatplayer.main.entities;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyAlbumDAO;

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

	@Override
	public Boolean delete() {
		// TODO Auto-generated method stub
		return null;
	}

	public Bitmap getCover() {
		return cover;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		if(name==null)
		{
			name = getDao().getName(this);
		}
		return name;
	}
	/**
	 * Khong ho tro DISK SOURCE vi ly do Disk khong to chuc theo CSDL
	 * @return
	 */
	public ArrayList<MySong> getSongs() {
		if(songs==null)
		{
			songs = getDao().getSongs(this);
		}
		return songs;
	}

	@Override
	public Integer insert() {
		// TODO Auto-generated method stub
		return getDao().insert(this);
	}

	@Override
	public void reset() {

	}

	public void setCover(Bitmap cover) {
		this.cover = cover;
	}

	public void setId(Integer id_) {
		id = id_ == null ? 0 : id_;
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
