package com.qdcatplayer.main.BackgroundTasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import android.media.MediaPlayer;

import com.qdcatplayer.main.Entities.MySong;

public class MyPlayer {
	public MyPlayer(MySong current, ArrayList<MySong> list) {
		setNew(current, list);
	}
	public Boolean setNew(MySong current, ArrayList<MySong> list)
	{
		if(getMediaPlayer()!=null)
		{
			if(getMediaPlayer().isPlaying())
			{
				getMediaPlayer().stop();
			}
		}
		if(r_tmp==null) r_tmp = new Random();
		if(mediaPlayer==null) mediaPlayer = new MediaPlayer();
		setCurrentSong(current);
		setSongsList(list==null?new ArrayList<MySong>():list);
		playedList = new HashMap<Integer, MySong>();
		playedStack = new Stack<MySong>();
		if(isReady())
		{
			playedList.put(getCurrentIndex(), getCurrentSong());
			playedStack.push(getCurrentSong());
		}
		//final
		prepareMedia();
		return true;
	}
	public Integer getCurrentIndex()
	{
		if(getCurrentSong()!=null && getSongsList().size()>0)
		{
			return getSongsList().indexOf(getCurrentSong());
		}
		else
		{
			return -1;
		}
	}
	private void prepareMedia()
	{
		if(isReady())
		{
			try {
				getMediaPlayer().reset();
				getMediaPlayer().setDataSource(getCurrentSong().getPath().getAbsPath());
				getMediaPlayer().prepare();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public Boolean isReady()
	{
		return (getCurrentSong()!=null
				&& getSongsList()!=null 
				&& getSongsList().size()>0
				&& getMediaPlayer()!=null);
	}
	public Boolean Play()
	{
		if(isReady())
		{
			try{
				getMediaPlayer().start();
				return true;
			}catch(IllegalStateException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	public Boolean Pause()
	{
		if(isReady())
		{
			try{
				getMediaPlayer().pause();
				return true;
			}catch(IllegalStateException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	public Boolean Stop()
	{
		if(isReady())
		{
			prepareMedia();
		}
		return true;
	}
	
	public Boolean requestNextSong() {
		if(!Stop())
		{
			return false;
		}
		Integer index = getCurrentIndex();
		// Step 1: Calculate next index
		// check repeate mode
		if (getRepeatMode() == 0) {
			// check shuffle mode
			if (getShuffleMode()) {
				if (getPlayedList().size() < getSongsList().size()) {
					while (true) {
						index = r_tmp.nextInt(getSongsList().size());
						if (!getPlayedList().containsKey(index)) {
							break;
						}
					}
				} else {
					index = -1;
				}
			} else {
				index++;
				if (index >= getSongsList().size()) {
					index = -1;
				}
			}
		} else if (getRepeatMode() == 1) {

		} else if (getRepeatMode() == 2) {
			if (getShuffleMode()) {
				if (getPlayedList().size() >= getSongsList().size()) {
					getPlayedList().clear();
					// playedStack.clear(); to reduce memory
				}

				while (true) {
					index = r_tmp.nextInt(getSongsList().size());
					if (getPlayedList().containsKey(index)) {
						break;
					}
				}
			} else {
				index++;
				if (index >= getSongsList().size()) {
					index = 0;
				}
			}
		}
		// final
		if (index >= 0) {
			setCurrentSong(getSongsList().get(index));
			getPlayedStack().push(getCurrentSong());
			getPlayedList().put(index, getCurrentSong());
			prepareMedia();
			return true;
		}
		return false;
	}
	public Boolean requestPrevSong() {
		if(!Stop())
		{
			return false;
		}
		
		if (getRepeatMode() == 1) {
			
		} else if (getPlayedStack().size() >= 2) {
			getPlayedList()
					.remove(getPlayedStack().pop());
				setCurrentSong(getPlayedStack().lastElement());
		} else if(getPlayedStack().size() >= 1)
		{
			setCurrentSong(getPlayedStack().lastElement());
		}
		else
		{
			return false;
		}
		prepareMedia();
		return true;
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}
	public MySong getCurrentSong() {
		return currentSong;
	}
	public void setCurrentSong(MySong currentSong) {
		this.currentSong = currentSong;
	}
	public ArrayList<MySong> getSongsList() {
		return songsList;
	}
	public void setSongsList(ArrayList<MySong> songsList) {
		this.songsList = songsList;
	}
	public Boolean getShuffleMode() {
		return shuffleMode;
	}
	public void setShuffleMode(Boolean shuffleMode) {
		this.shuffleMode = shuffleMode;
	}
	public Integer getRepeatMode() {
		return repeatMode;
	}
	public void setRepeatMode(Integer repeatMode) {
		this.repeatMode = repeatMode;
	}
	public HashMap<Integer, MySong> getPlayedList() {
		return playedList;
	}
	public void setPlayedList(HashMap<Integer, MySong> playedList) {
		this.playedList = playedList;
	}
	public Stack<MySong> getPlayedStack() {
		return playedStack;
	}
	public void setPlayedStack(Stack<MySong> playedStack) {
		this.playedStack = playedStack;
	}
	public Boolean isPlaying()
	{
		if(!isReady())
		{
			return false;
		}
		return getMediaPlayer().isPlaying();
	}
	public Integer getPlayedCount()
	{
		if(!isReady())
		{
			return 0;
		}
		return getPlayedList().size();
	}
	public Integer getTotalCount()
	{
		if(!isReady())
		{
			return 0;
		}
		return getSongsList().size();
	}
	public Integer getDuration()
	{
		if(!isReady())
		{
			return 0;
		}
		return getMediaPlayer().getDuration();
	}
	
	private Random r_tmp = null;
	private MediaPlayer mediaPlayer = null;
	private MySong currentSong = null;
	private ArrayList<MySong> songsList = null;
	private Boolean shuffleMode = false;
	private Integer repeatMode = 0;
	private HashMap<Integer, MySong> playedList = null;
	private Stack<MySong> playedStack = null;
	/**
	 * End
	 */
	public void seekTo(int currentPosition) {
		if(!isReady())
		{
			return;
		}
		getMediaPlayer().seekTo(currentPosition);
	}
	public Integer getCurrentPosition() {
		if(!isReady())
		{
			return 0;
		}
		return getMediaPlayer().getCurrentPosition();
	}
	public void release() {
		if(isReady())
		{
			getMediaPlayer().stop();
			getMediaPlayer().release();
		}
	}
	
}
