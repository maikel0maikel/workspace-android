package com.qdcatplayer.main.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.qdcatplayer.main.DAOs.MySong_MyPlayListDAO;

@DatabaseTable(tableName = "MySong_MyPlayLists")
public class MySong_MyPlayList extends
		_MyEntityAbstract<MySong_MyPlayListDAO, MySong_MyPlayList> {

	public static final String PLAYLIST_ID = "playlist_id";
	public static final String SONG_ID = "song_id";
	@DatabaseField(foreign = true, canBeNull = false)
	private MyPlayList playlist = null;
	@DatabaseField(foreign = true, canBeNull = false)
	private MySong song = null;

	public MySong_MyPlayList() {

	}

	@Override
	public void reset() {
		super.reset();
		song = null;
		playlist = null;
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

	public MySong getSong() {
		// TODO Auto-generated method stub
		return song;
	}

	public MyPlayList getPlaylist() {
		return playlist;
	}

	public void setPlaylist(MyPlayList playlist) {
		this.playlist = playlist;
	}

	public void setSong(MySong song) {
		this.song = song;
	}

}
