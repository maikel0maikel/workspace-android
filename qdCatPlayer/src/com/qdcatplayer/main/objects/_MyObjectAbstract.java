package com.qdcatplayer.main.objects;

import com.j256.ormlite.field.DatabaseField;

public abstract class _MyObjectAbstract<T> implements _MyObjectInterface<T> {
	/**
	 * Table always has _id field for PK
	 */
	@DatabaseField(generatedId=true)
	protected Integer _id=null;
	/**
	 * work directly with its own custom My...DAO
	 */
	protected T _dao=null;
	/**
	 * Must be declare to make sure ORMLite can reference to
	 */
	public _MyObjectAbstract() {
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
}
