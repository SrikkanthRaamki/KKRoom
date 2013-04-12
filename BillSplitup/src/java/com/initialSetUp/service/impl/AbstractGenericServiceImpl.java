package com.initialSetUp.service.impl;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.hibernate.ReplicationMode;
import org.springframework.transaction.annotation.Transactional;

import com.initialSetUp.dao.GenericDAO;
import com.initialSetUp.service.GenericService;

/**
 * The Class AbstractGenericServiceImpl.
 */
@Transactional(propagation = SUPPORTS)
public class AbstractGenericServiceImpl<T, PK extends Serializable> implements GenericService<Object, PK> {

	/** The log. */
//	protected final Logger log = SecurityUtility.getLogger(getClass());

	/** The generic dao. */
	protected GenericDAO<Object, PK> genericDAO;

	/**
	 * Instantiates a new abstract generic service impl.
	 */
	public AbstractGenericServiceImpl() {
//		if(log.isDebugEnabled()) {
//			log.log(KMAPILoggingLevel.KMAPI_DEBUG,"Creating and instance of " + getClass().getSimpleName());
//		}
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#getAll()
	 */
	public List<Object> getAll() {
		return genericDAO.getAll();
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#findAll()
	 */
	public List<Object> findAll() {
		return genericDAO.findAll();
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#get(java.io.Serializable)
	 */
	public Object get(PK id) {
		return genericDAO.get(id);
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#get(java.util.Set)
	 */
	public List<Object> get(Set<PK> ids) {
		return genericDAO.get(ids);
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#exists(java.io.Serializable)
	 */
	public boolean exists(PK id) {
		return genericDAO.exists(id);
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#save(java.lang.Object)
	 */
	@Transactional(propagation = REQUIRED)
	public Object save(Object object) {
		Object returnObject = genericDAO.save(object);
		afterSave(object);
		return returnObject;
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#saveAll(java.util.Set)
	 */
	@Transactional(propagation = REQUIRED)
	public Set<Object> saveAll(Set<Object> objects) {
		Set<Object> returnSet = genericDAO.saveAll(objects);
		for (Object object : returnSet) {
			afterSave(object);
		}
		return returnSet;
	}

	/**
	 * After save.
	 *
	 * @param object the object
	 */
	public void afterSave(Object object) {

	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#delete(java.io.Serializable)
	 */
	@Transactional(propagation = REQUIRED)
	public void delete(PK id) {
		genericDAO.delete(id);
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#deleteAll(java.util.Set)
	 */
	@Transactional(propagation = REQUIRED)
	public void deleteAll(Set<Object> objects) {
		genericDAO.deleteAll(objects);
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#delete(java.lang.Object)
	 */
	@Transactional(propagation = REQUIRED)
	public void delete(Object object) {
		genericDAO.delete(object);
	}

	/**
	 * Gets the generic dao.
	 *
	 * @return the generic dao
	 */
	public GenericDAO<Object, PK> getGenericDAO() {
		return genericDAO;
	}

	/**
	 * Sets the generic dao.
	 *
	 * @param genericDAO the generic dao
	 */
	public void setGenericDAO(GenericDAO<Object, PK> genericDAO) {
		this.genericDAO = genericDAO;
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#flush()
	 */
	public void flush() {
		genericDAO.flush();
	}

	/* (non-Javadoc)
	 * @see com.boeing.kss.service.GenericService#replicate(java.lang.Object, org.hibernate.ReplicationMode)
	 */
	@Transactional(propagation = REQUIRED)
	public void replicate(Object object, ReplicationMode replicationMode) {
		getGenericDAO().replicate(object, replicationMode);
	}

	@Transactional(propagation = REQUIRED)
	public void evict(Object object){
		getGenericDAO().evict(object);
	}

	@Transactional(propagation = REQUIRED)
	public Object merge(Object object) {
		Object returnObject = genericDAO.merge(object);
		return returnObject;
	}


}
