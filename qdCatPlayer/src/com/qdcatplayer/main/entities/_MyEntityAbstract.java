package com.qdcatplayer.main.entities;

import com.j256.ormlite.field.DatabaseField;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs._GlobalDAOInterface;
import com.qdcatplayer.main.DAOs._MyDAOInterface;

public abstract class _MyEntityAbstract<T> implements _MyEntityInterface<T>, _GlobalDAOInterface {
	/**
	 * Table always has _id field for PK
	 */
	@DatabaseField(generatedId=true)
	protected Integer _id=null;
	/**
	 * work directly with its own custom My...DAO, nothing else
	 */
	protected T _dao=null;
	/**
	 * Must be declare to make sure ORMLite can reference to
	 */
	public _MyEntityAbstract() {
	}
	public Boolean setDao(T dao) {
		_dao=dao;
		return true;
	}
	@Override
	public T getDao() {
		return _dao;
	}
	
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return _id;
	}

	@Override
	public Boolean setId(Integer id) {
		// TODO Auto-generated method stub
		_id=id;
		return true;
	}
	@Override
	public GlobalDAO getGlobalDAO() {
		if(getDao()!=null)
		{
			return ((_GlobalDAOInterface)getDao()).getGlobalDAO();
		}
		return null;
	}
}
