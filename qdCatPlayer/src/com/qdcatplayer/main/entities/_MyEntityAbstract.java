package com.qdcatplayer.main.entities;

import com.j256.ormlite.field.DatabaseField;
import com.qdcatplayer.main.DAOs.GlobalDAO;
import com.qdcatplayer.main.DAOs.MySource;
import com.qdcatplayer.main.DAOs._GlobalDAOInterface;
import com.qdcatplayer.main.DAOs._MyDAOAbstract;
import com.qdcatplayer.main.DAOs._MyDAOInterface;

public abstract class _MyEntityAbstract<T,K> implements _MyEntityInterface<T,K>, _GlobalDAOInterface {
	/**
	 * Table always has _id field for PK
	 */
	@DatabaseField(generatedId=true)
	protected Integer id=null;
	/**
	 * Chi dinh obj da duoc cap nhat du lieu
	 */
	protected Boolean loaded = false;
	public static final String ID_F = "id";
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
	/**
	 * Neu da load roi thi khi goi ham nay se
	 * khong co tac dung toi chung nao reset
	 */
	@Override
	public void load() {
		if(loaded==false)
		{
			((_MyDAOInterface<T, K>)getDao()).load((K)this);
			loaded = true;
		}
	}
}
