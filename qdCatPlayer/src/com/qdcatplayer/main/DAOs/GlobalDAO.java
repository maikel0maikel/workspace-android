package com.qdcatplayer.main.DAOs;

import android.content.Context;

/**
 * My Custom Global DAO for cross access and use Cached supported
 * 
 * @author quocdunginfo
 * 
 */
public class GlobalDAO {
	private Context _ctx = null;
	private MyAlbumDAO _myAlbumDAO = null;
	private MyArtistDAO _myArtistDAO = null;
	private MyBitrateDAO _myBitrateDAO = null;
	private MyFolderDAO _myFolderDAO = null;
	private MyFormatDAO _myFormatDAO = null;
	private MyPathDAO _myPathDAO = null;
	private MySongDAO _mySongDAO = null;
	private MyPlayListDAO _myPlayListDAO = null;
	private MySong_MyPlayListDAO _mySong_MyPlayListDAO = null;
	private Integer source = 0;

	public GlobalDAO(Context ctx) {
		_ctx = ctx;
	}

	public Context getContext() {
		return _ctx;
	}

	public MyAlbumDAO getMyAlbumDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_myAlbumDAO != null) {
			return _myAlbumDAO;
		}
		// init new one
		_myAlbumDAO = new MyAlbumDAO(getContext(), this);
		return _myAlbumDAO;
	}

	public MyArtistDAO getMyArtistDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_myArtistDAO != null) {
			return _myArtistDAO;
		}
		// init new one
		_myArtistDAO = new MyArtistDAO(getContext(), this);
		return _myArtistDAO;
	}

	public MyBitrateDAO getMyBitrateDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_myBitrateDAO != null) {
			return _myBitrateDAO;
		}
		// init new one
		_myBitrateDAO = new MyBitrateDAO(getContext(), this);
		return _myBitrateDAO;
	}

	public MyFolderDAO getMyFolderDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_myFolderDAO != null)// very useful because save CPU and memory
		{
			return _myFolderDAO;
		}
		// init new one
		_myFolderDAO = new MyFolderDAO(getContext(), this);
		return _myFolderDAO;
	}

	public MyFormatDAO getMyFormatDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_myFormatDAO != null) {
			return _myFormatDAO;
		}
		// init new one
		_myFormatDAO = new MyFormatDAO(getContext(), this);
		return _myFormatDAO;
	}

	public MyPathDAO getMyPathDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_myPathDAO != null) {
			return _myPathDAO;
		}
		// init new one
		_myPathDAO = new MyPathDAO(getContext(), this);
		return _myPathDAO;
	}

	public MySongDAO getMySongDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_mySongDAO != null) {
			return _mySongDAO;
		}
		// init new one
		_mySongDAO = new MySongDAO(getContext(), this);
		return _mySongDAO;
	}

	public MyPlayListDAO getMyPlayListDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_myPlayListDAO != null) {
			return _myPlayListDAO;
		}
		// init new one
		_myPlayListDAO = new MyPlayListDAO(getContext(), this);
		return _myPlayListDAO;
	}

	public MySong_MyPlayListDAO getMySong_MyPlayListDAO() {
		// require
		if (getContext() == null) {
			return null;
		}
		// lazy
		if (_mySong_MyPlayListDAO != null) {
			return _mySong_MyPlayListDAO;
		}
		// init new one
		_mySong_MyPlayListDAO = new MySong_MyPlayListDAO(getContext(), this);
		return _mySong_MyPlayListDAO;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public void release() {
		if (_myAlbumDAO != null) {
			_myAlbumDAO.release();
		}
		if (_myArtistDAO != null) {
			_myArtistDAO.release();
		}
		if (_myBitrateDAO != null) {
			_myBitrateDAO.release();
		}
		if (_myFolderDAO != null) {
			_myFolderDAO.release();
		}
		if (_myFormatDAO != null) {
			_myFormatDAO.release();
		}
		if (_myPathDAO != null) {
			_myPathDAO.release();
		}
		if (_mySongDAO != null) {
			_mySongDAO.release();
		}
		if (_myPlayListDAO != null) {
			_myPlayListDAO.release();
		}
		if (_mySong_MyPlayListDAO != null) {
			_mySong_MyPlayListDAO.release();
		}
	}
}
