package com.qdcatplayer.main.DAOs;

import android.content.Context;
/**
 * My Custom Global DAO for cross access and use
 * Cached supported
 * @author quocdunginfo
 *
 */
public class GlobalDAO {
	private Context _ctx=null;
	private MyAlbumDAO _myAlbumDAO=null;
	private MyArtistDAO _myArtistDAO=null;
	private MyBitrateDAO _myBitrateDAO=null;
	private MyFolderDAO _myFolderDAO=null;
	private MyFormatDAO _myFormatDAO=null;
	private MyPathDAO _myPathDAO=null;
	private MySongDAO _mySongDAO=null;
	private Integer source=0;
	public GlobalDAO(Context ctx) {
		_ctx = ctx;
	}
	public Context getContext() {
		return _ctx;
	}
	public MyAlbumDAO getMyAlbumDAO() {
		//require
		if(getContext()==null)
		{
			return null;
		}
		//lazy
		if(_myAlbumDAO!=null)
		{
			return _myAlbumDAO;
		}
		//init new one
		_myAlbumDAO = new MyAlbumDAO(getContext(), this);
		return _myAlbumDAO;
	}
	public MyArtistDAO getMyArtistDAO() {
		//require
		if(getContext()==null)
		{
			return null;
		}
		//lazy
		if(_myArtistDAO!=null)
		{
			return _myArtistDAO;
		}
		//init new one
		_myArtistDAO = new MyArtistDAO(getContext(), this);
		return _myArtistDAO;
	}
	public MyBitrateDAO getMyBitrateDAO() {
		//require
		if(getContext()==null)
		{
			return null;
		}
		//lazy
		if(_myBitrateDAO!=null)
		{
			return _myBitrateDAO;
		}
		//init new one
		_myBitrateDAO = new MyBitrateDAO(getContext(), this);
		return _myBitrateDAO;
	}
	public MyFolderDAO getMyFolderDAO() {
		//require
		if(getContext()==null)
		{
			return null;
		}
		//lazy
		if(_myFolderDAO!=null)//very useful because save CPU and memory
		{
			return _myFolderDAO;
		}
		//init new one
		_myFolderDAO = new MyFolderDAO(getContext(), this);
		return _myFolderDAO;
	}
	public MyFormatDAO getMyFormatDAO() {
		//require
		if(getContext()==null)
		{
			return null;
		}
		//lazy
		if(_myFormatDAO!=null)
		{
			return _myFormatDAO;
		}
		//init new one
		_myFormatDAO = new MyFormatDAO(getContext(), this);
		return _myFormatDAO;
	}
	public MyPathDAO getMyPathDAO() {
		//require
		if(getContext()==null)
		{
			return null;
		}
		//lazy
		if(_myPathDAO!=null)
		{
			return _myPathDAO;
		}
		//init new one
		_myPathDAO = new MyPathDAO(getContext(), this);
		return _myPathDAO;
	}
	public MySongDAO getMySongDAO() {
		//require
		if(getContext()==null)
		{
			return null;
		}
		//lazy
		if(_mySongDAO!=null)
		{
			return _mySongDAO;
		}
		//init new one
		_mySongDAO = new MySongDAO(getContext(), this);
		return _mySongDAO;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
}
