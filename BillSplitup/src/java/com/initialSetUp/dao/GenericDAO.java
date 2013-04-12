package com.initialSetUp.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.LockMode;
import org.hibernate.ReplicationMode;
import org.springframework.dao.DataAccessException;

/**
 * The Interface GenericDAO.
 */
public interface GenericDAO<T, PK extends Serializable> {

	/**
	 * Exists.
	 *
	 * @param id the id
	 *
	 * @return true, if successful
	 */
	boolean exists(PK id);

	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<T> getAll();

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
	 * Merge.
	 *
	 * @param object the object
	 *
	 * @return the t
	 */
	public T merge(T object);

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
	 * Find all.
	 *
	 * @return the list< t>
	 */
	List<T> findAll();

	/**
	 * Find by named query.
	 *
	 * @param namedQuery the named query
	 *
	 * @return the list< t>
	 */
	List<T> findByNamedQuery(String namedQuery);

	/**
	 * Find by named query.
	 *
	 * @param namedQuery the named query
	 * @param name the name
	 * @param param the param
	 *
	 * @return the list< t>
	 */
	List<T> findByNamedQuery(String namedQuery, String name, Object param);

	/**
	 * Find by named query.
	 *
	 * @param namedQuery the named query
	 * @param names the names
	 * @param params the params
	 *
	 * @return the list< t>
	 */
	List<T> findByNamedQuery(String namedQuery, String[] names, Object[] params);

	/**
	 * Find by named query.
	 *
	 * @param namedQuery the named query
	 * @param params the params
	 *
	 * @return the list< t>
	 */
	List<T> findByNamedQuery(String namedQuery, Map<String,Object> params);

	/**
	 * Find by named query.
	 *
	 * @param namedQuery the named query
	 * @param params the params
	 *
	 * @return the list< t>
	 */
	List<T> findByNamedQuery(String namedQuery, Map.Entry<String,Object>... params);

	/**
	 * Find single result.
	 *
	 * @param namedQuery the named query
	 *
	 * @return the t
	 */
	T findSingleResult(String namedQuery);

	/**
	 * Find single result.
	 *
	 * @param namedQuery the named query
	 * @param name the name
	 * @param param the param
	 *
	 * @return the t
	 */
	T findSingleResult(String namedQuery, String name, Object param);

	/**
	 * Find single result.
	 *
	 * @param namedQuery the named query
	 * @param names the names
	 * @param params the params
	 *
	 * @return the t
	 */
	T findSingleResult(String namedQuery, String[] names, Object[] params);

	/**
	 * Find single result.
	 *
	 * @param namedQuery the named query
	 * @param params the params
	 *
	 * @return the t
	 */
	T findSingleResult(String namedQuery, Map<String,Object> params);

	/**
	 * Find single result.
	 *
	 * @param namedQuery the named query
	 * @param params the params
	 *
	 * @return the t
	 */
	T findSingleResult(String namedQuery, Map.Entry<String,Object>... params);

	/**
	 * Gets the persistent class.
	 *
	 * @return the persistent class
	 */
	@SuppressWarnings("unchecked")
	Class getPersistentClass();

	/**
	 * Replicate.
	 *
	 * @param entity the entity
	 * @param replicationMode the replication mode
	 *
	 * @throws DataAccessException the data access exception
	 */
	void replicate(Object entity, ReplicationMode replicationMode) throws DataAccessException;

	/**
	 * Replicate.
	 *
	 * @param entityName the entity name
	 * @param entity the entity
	 * @param replicationMode the replication mode
	 *
	 * @throws DataAccessException the data access exception
	 */
	void replicate(String entityName, Object entity, ReplicationMode replicationMode) throws DataAccessException;

	/**
	 * Gets the.
	 *
	 * @param id the id
	 * @param lockMode the lock mode
	 *
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	T get(PK id, LockMode lockMode);

	void evict(T object);


}
