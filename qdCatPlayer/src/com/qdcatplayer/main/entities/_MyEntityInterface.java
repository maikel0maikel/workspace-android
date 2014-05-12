package com.qdcatplayer.main.Entities;

public interface _MyEntityInterface<T, K> {
	public Integer getId();

	public void setId(Integer id);

	/**
	 * Load all properties
	 */
	public void load();

	public Boolean isLoaded();

	public void setLoaded(Boolean loaded);

	public void reset();

	public void setDao(T dao);

	public T getDao();

	public Integer insert();

	public Boolean update();

	public Boolean delete();
}
