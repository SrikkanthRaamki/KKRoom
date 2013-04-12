package com.initialSetUp.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.hibernate.ReplicationMode;

/**
 * The Interface GenericService.
 */
public interface GenericService<T, PK extends Serializable> {

	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<T> getAll();

	/**
	 * Find all.
	 *
	 * @return the list< t>
	 */
	List<T> findAll();

	/**
	 * Gets the.
	 *
	 * @param id the id
	 *
	 * @return the t
	 */
	T get(PK id);

	/**
	 * Gets the.
	 *
	 * @param ids the ids
	 *
	 * @return the list< t>
	 */
	List<T> get(Set<PK> ids);

	/**
	 * Exists.
	 *
	 * @param id the id
	 *
	 * @return true, if successful
	 */
	boolean exists(PK id);

	/**
	 * Save.
	 *
	 * @param object the object
	 *
	 * @return the t
	 */
	T save(T object);

	/**
	 * Save all.
	 *
	 * @param objects the objects
	 *
	 * @return the set< t>
	 */
	Set<T> saveAll(Set<T> objects);

	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	void delete(PK id);

	/**
	 * Delete.
	 *
	 * @param object the object
	 */
	void delete(T object);

	/**
	 * Delete all.
	 *
	 * @param objects the objects
	 */
	void deleteAll(Set<T> objects);

	/**
	 * Flush.
	 */
	void flush();

	/**
	 * Replicate.
	 *
	 * @param object the object
	 * @param replicationMode the replication mode
	 */
	void replicate(T object, ReplicationMode replicationMode);

	public void evict(T object);

	T merge(T Object);

}
