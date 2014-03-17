package com.qdcatplayer.main.entities;

import com.j256.ormlite.field.DatabaseField;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs._GlobalDAOInterface;
import com.qdcatplayer.main.DAOs._MyDAOInterface;

public abstract class _MyEntityAbstract<T> implements _MyEntityInterface<T>, _GlobalDAOInterface {
	/**
	 * Table always has _id field for PK
	 */
	@DatabaseField(generatedId=true, useGetSet=true)
	protected Integer id=null;
	/**
	 * work directly with its own custom My...DAO, nothing else
	 */
	protected T dao=null;
	/**
	 * Must be declare to make sure ORMLite can reference to
	 */
	public _MyEntityAbstract() {
	}
	public void setDao(T dao_) {
		dao=dao_;
	}
	@Override
	public T getDao() {
		return dao;
	}
	
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Integer id_) {
		// TODO Auto-generated method stub
		id=id_;
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
