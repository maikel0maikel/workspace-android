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
	public GlobalDAO(Context ctx) {
		_ctx = ctx;
	}
	public Context getContext() {
		return _ctx;
	}
	/**
	 * Chua cac Custom DAO de su dung lai
	 */
	private MySongDAO _mySongDAO=null;
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
	private MyFolderDAO _myFolderDAO=null;
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
	private MyPathDAO _myPathDAO=null;
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
	private MyAlbumDAO _myAlbumDAO=null;
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
	private MyBitrateDAO _myBitrateDAO=null;
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
}
