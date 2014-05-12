package com.qdcatplayer.main.Entities;

import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MyPlayListDAO;
import com.qdcatplayer.main.Libraries.MyStringHelper;

@DatabaseTable(tableName = "MyPlayLists")
public class MyPlayList extends _MyEntityAbstract<MyPlayListDAO, MyPlayList> {
	/**
	 * 
	 */
	public static final String NAME_F = "name";

	@DatabaseField(unique = true)
	private String name = null;

	private ArrayList<MySong> songs = null;

	public MyPlayList() {

	}

	public MyPlayList(String name) {
		setName(name);
	}

	public String getName() {
		super.load();
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

	public void setSongs(ArrayList<MySong> songs) {
		this.songs = songs;
	}

	/**
	 * No duplicate allowed
	 * 
	 * @param obj
	 */
	public void addSong(MySong obj) {
		if (songs == null) {
			songs = new ArrayList<MySong>();
		}
		if (_songExist(obj) != null) {
			return;
		}
		songs.add(obj);
	}

	public void removeSong(MySong obj) {
		if (songs == null) {
			return;
		}
		MySong tmp = _songExist(obj);
		if (tmp != null) {
			songs.remove(tmp);
		}
	}

	/**
	 * Kiểm tra song có trong playlist này chưa
	 * 
	 * @param obj
	 * @return Song match
	 */
	private MySong _songExist(MySong obj) {
		if (songs == null) {
			return null;
		}
		for (MySong item : songs) {
			if (item.getId() == obj.getId())// do not need compare absPath
			{
				return item;
			}
		}
		return null;
	}

	@Override
	public void reset() {
		super.reset();
		name = null;
		songs = null;
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
