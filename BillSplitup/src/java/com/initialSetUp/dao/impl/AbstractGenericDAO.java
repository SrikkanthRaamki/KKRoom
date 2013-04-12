package com.initialSetUp.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.initialSetUp.dao.GenericDAO;

/**
 * The Class AbstractGenericDAO.
 */
public abstract class AbstractGenericDAO<T, PK extends Serializable> implements GenericDAO<Object, PK> {

    /** The log. */
  //  final Logger log = SecurityUtility.getLogger(getClass());

    /** The persistent class. */
    private  Class<T> persistentClass;

    /** The hibernate template. */
    HibernateTemplate hibernateTemplate;

    /**
     * Instantiates a new abstract generic dao.
     */
    @SuppressWarnings("unchecked")
    public AbstractGenericDAO() {
      this.persistentClass = (Class<T>)ClassUtils.getTypeArguments(AbstractGenericDAO.class, getClass()).get(0);
    }

    /**
     * Instantiates a new abstract generic dao.
     *
     * @param persistentClass the persistent class
     */
    public AbstractGenericDAO(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * Sets the session factory.
     *
     * @param sessionFactory the new session factory
     */
    @Autowired
    public void setSessionFactory(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
    	hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#exists(java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        Object entity = (Object) getHibernateTemplate().get(this.persistentClass, id);
        return entity != null;
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#getAll()
     */
    public List<Object> getAll() {
        return    (List<Object>) generify(getHibernateTemplate().loadAll(persistentClass));
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#get(java.io.Serializable)
     */
    @SuppressWarnings("unchecked")
    public Object get(PK id) {
//        if (log.isDebugEnabled()) {
//            log.log(KMAPILoggingLevel.KMAPI_DEBUG, "Getting entity for " + persistentClass.getSimpleName() + " with id = " + id);
//        }
        return  (Object) getHibernateTemplate().get(this.persistentClass, id);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#get(java.io.Serializable, org.hibernate.LockMode)
     */
    @SuppressWarnings("unchecked")
    public Object get(PK id, LockMode lockMode) {
//        if (log.isDebugEnabled()) {
//            log.log(KMAPILoggingLevel.KMAPI_DEBUG, "Getting entity for " + persistentClass.getSimpleName() + " with id = " + id
//                    + " and lockMode = " + lockMode
//            );
//        }
        return (Object) getHibernateTemplate().get(this.persistentClass, id, lockMode);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#get(java.util.Set)
     */
    public List<Object> get(Set<PK> ids) {
    	return generify(getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(persistentClass).add(Restrictions.in("id", ids))));
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#save(java.lang.Object)
     */
    public Object save(Object object) {
        getHibernateTemplate().saveOrUpdate(object);
        return object;
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#saveAll(java.util.Set)
     */
    public Set<Object> saveAll(Set<Object> objects) {
        getHibernateTemplate().saveOrUpdateAll(objects);
        return objects;
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#merge(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public Object merge(Object object) {
        return (Object)getHibernateTemplate().merge(object);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#delete(java.io.Serializable)
     */
    public void delete(PK id) {
        getHibernateTemplate().delete(get(id));
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#delete(java.lang.Object)
     */
    public void delete(Object object) {
        getHibernateTemplate().delete(object);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#deleteAll(java.util.Set)
     */
    public void deleteAll(Set<Object> objects) {
        getHibernateTemplate().deleteAll(objects);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#flush()
     */
    public void flush() {
        getHibernateTemplate().flush();
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findAll()
     */
    @SuppressWarnings("unchecked")
	public List<Object> findAll() {
//        if (log.isDebugEnabled()) {
//            log.log(KMAPILoggingLevel.KMAPI_DEBUG, "Getting all entities for " + persistentClass.getSimpleName());
//        }

        try {
//            if (log.isDebugEnabled()) {
//                log.log(KMAPILoggingLevel.KMAPI_DEBUG, "Attempted to used named query " + persistentClass.getSimpleName() + ".findAll");
//            }
            List results = (List) getHibernateTemplate().execute(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    Query q = session.getNamedQuery(persistentClass.getSimpleName() + ".findAll");
                    return q.list();
                }
            });
            return generify(results);
        } catch (HibernateException ex) {
//            log.log(KMAPILoggingLevel.KMAPI_WARN, ex);
            return getAll();
        }
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findByNamedQuery(java.lang.String)
     */
   public List<Object> findByNamedQuery(String namedQuery) {
        return findByNamedQuery(
                namedQuery,
                new String[0],
                new Object[0]
        );
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findByNamedQuery(java.lang.String, java.lang.String, java.lang.Object)
     */
    public List<Object> findByNamedQuery(String namedQuery, String name, Object param) {
        List<String> names = new ArrayList<String>();
        names.add(name);
        List<Object> params = new ArrayList<Object>();
        params.add(param);
        return findByNamedQuery(
                namedQuery,
                names,
                params);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findByNamedQuery(java.lang.String, java.lang.String[], java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
	public List<Object> findByNamedQuery(String namedQuery, String names[], Object params[]) {
        String simpleName = getPersistentClass().getSimpleName();
        if (!namedQuery.startsWith(simpleName)) {
            namedQuery = simpleName + "." + namedQuery;
        }

        List results = getHibernateTemplate().findByNamedQueryAndNamedParam(
                namedQuery,
                names,
                params
        );

        return generify(results);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findByNamedQuery(java.lang.String, java.util.Map)
     */
    public List<Object> findByNamedQuery(String namedQuery, Map<String, Object> params) {
        List<String> names = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            names.add(entry.getKey());
            values.add(entry.getValue());
        }

        return findByNamedQuery(namedQuery, names, values);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findByNamedQuery(java.lang.String, java.util.Map.Entry<java.lang.String,java.lang.Object>[])
     */
    public List<Object> findByNamedQuery(String namedQuery, Map.Entry<String, Object>... params) {
        List<String> names = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : params) {
            names.add(entry.getKey());
            values.add(entry.getValue());
        }
        return findByNamedQuery(namedQuery, names, values);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findSingleResult(java.lang.String)
     */
    public Object findSingleResult(String namedQuery) {
        return findSingleResult(
                namedQuery,
                new ArrayList<String>(),
                new ArrayList<Object>()
        );
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findSingleResult(java.lang.String, java.lang.String, java.lang.Object)
     */
    public Object findSingleResult(String namedQuery, String name, Object param) {
        List<String> names = new ArrayList<String>();
        names.add(name);
        List<Object> params = new ArrayList<Object>();
        params.add(param);
        return findSingleResult(
                namedQuery,
                names,
                params);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findSingleResult(java.lang.String, java.lang.String[], java.lang.Object[])
     */
    public Object findSingleResult(String namedQuery, String[] names, Object[] params) {
        return findSingleResult(namedQuery, Arrays.asList(names), Arrays.asList(params));
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findSingleResult(java.lang.String, java.util.Map)
     */
    public Object findSingleResult(String namedQuery, Map<String, Object> params) {
        List<String> names = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            names.add(entry.getKey());
            values.add(entry.getValue());
        }

        return findSingleResult(namedQuery, names, values);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#findSingleResult(java.lang.String, java.util.Map.Entry<java.lang.String,java.lang.Object>[])
     */
    public Object findSingleResult(String namedQuery, Map.Entry<String, Object>... params) {
        List<String> names = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        for (Map.Entry<String, Object> entry : params) {
            names.add(entry.getKey());
            values.add(entry.getValue());
        }
        return findSingleResult(namedQuery, names, values);
    }

    /**
     * Find single result.
     *
     * @param namedQuery the named query
     * @param names the names
     * @param params the params
     *
     * @return the t
     */
    public Object findSingleResult(String namedQuery, List<String> names, List<Object> params) {
        List<Object> results = findByNamedQuery(namedQuery, names, params);

        if (results == null || results.size() == 0) { //no results
            return null;
        } else if (results.size() == 1) {             //the normal case
            return results.get(0);
        } else {                                      //cases where joins return the same instance multiple times
            int size = results.size();
		    Object first = results.get(0);
		    for ( int i=1; i<size; i++ ) {
			    if ( results.get(i)!=first ) {
				    throw new NonUniqueResultException( results.size() );
			    }
		    }
		    return generify(first);
        }
    }

    /**
     * Find by named query.
     *
     * @param namedQuery the named query
     * @param names the names
     * @param params the params
     *
     * @return the list< t>
     */
    public List<Object> findByNamedQuery(String namedQuery, List<String> names, List params) {
        String[] stringNames = names.toArray(new String[names.size()]);
        return  findByNamedQuery(namedQuery, stringNames, params.toArray());
    }

    /**
     * Generify.
     *
     * @param results the results
     *
     * @return the list< t>
     *
     * @throws ClassCastException the class cast exception
     */
    @SuppressWarnings("unchecked")
    protected List<Object> generify(List<Object> results) throws ClassCastException {
        ArrayList<Object> genericResults = new ArrayList<Object>(results.size());
        for (Object o : results) {
            genericResults.add((Object) o);
        }
        return genericResults;
    }

    /**
     * Generify.
     *
     * @param object the object
     *
     * @return the t
     *
     * @throws ClassCastException the class cast exception
     */
    @SuppressWarnings("unchecked")
    protected Object generify(Object object) throws ClassCastException {
        return (Object) object;
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#getPersistentClass()
     */
    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#replicate(java.lang.Object, org.hibernate.ReplicationMode)
     */
    public void replicate(Object entity, ReplicationMode replicationMode) throws DataAccessException {
        getHibernateTemplate().replicate(entity, replicationMode);
    }

    /* (non-Javadoc)
     * @see com.boeing.kss.dao.GenericDAO#replicate(java.lang.String, java.lang.Object, org.hibernate.ReplicationMode)
     */
    public void replicate(String entityName, Object entity, ReplicationMode replicationMode) throws DataAccessException {
        getHibernateTemplate().replicate(entityName, entity, replicationMode);
    }

	/**
	 * Gets the hibernate template.
	 *
	 * @return the hibernate template
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * Sets the hibernate template.
	 *
	 * @param hibernateTemplate the new hibernate template
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void evict(Object object){
		getHibernateTemplate().evict(object);
	}

}
