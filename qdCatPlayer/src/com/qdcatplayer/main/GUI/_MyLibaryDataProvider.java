package com.qdcatplayer.main.GUI;

import java.util.ArrayList;

import com.qdcatplayer.main.Entities.MyAlbum;
import com.qdcatplayer.main.Entities.MyArtist;
import com.qdcatplayer.main.Entities.MyFolder;
import com.qdcatplayer.main.Entities.MyPlayList;
import com.qdcatplayer.main.Entities.MySong;

/*
 * Cung cap provider de fragment goi khi duoc hien thi
 * Activity phai implement
 */
public interface _MyLibaryDataProvider{
	public ArrayList<MySong> getSongs();
	public ArrayList<MyArtist> getArtists();
	public ArrayList<MyAlbum> getAlbums();
	public ArrayList<MyFolder> getFolders();
	public ArrayList<MyPlayList> getPlayLists();
	public ArrayList<MySong> getEnqueue();
}
