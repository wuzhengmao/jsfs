package org.mingy.kernel.orm.impl.hibernate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.mingy.kernel.bean.IEntity;
import org.mingy.kernel.orm.ICommonDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * 基于Hibernate的O-R映射的ICommonDao的接口实现
 * 
 * @author Mingy
 * 
 */
public class CommonDaoImpl extends HibernateDaoSupport implements ICommonDao {
	private static final Log logger = LogFactory.getLog(CommonDaoImpl.class);

	@Override
	public <T extends IEntity> void delete(Class<T> clazz, Serializable key) {
		T bean = this.findByKey(clazz, key);
		if (bean != null) {
			this.delete(bean);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("delete(" + clazz.getName() + ", " + key + ")");
		}
	}

	@Override
	public <T extends IEntity> void delete(T obj) {
		super.getHibernateTemplate().delete(obj);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(" + obj.getClass().getName() + ")");
		}
	}

	@Override
	public <T extends IEntity> void deleteAll(Class<T> cls) {
		String hql = "DELETE FROM " + cls.getName();
		// 执行批量删除操作
		long result = updateByQuery(hql, new Object[] {}, false, false);
		if (logger.isDebugEnabled()) {
			logger.debug("deleteAll(" + cls.getName() + "), total: " + result
					+ " rows.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IEntity> List<T> findAll(Class<T> cls) {
		String hql = "FROM " + cls.getName();
		return query(hql, new Object[] {}, false, false, -1, -1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IEntity> T findByKey(Class<T> cls, Serializable key) {
		return (T) super.getHibernateTemplate().get(cls, key);
	}

	@Override
	public <T extends IEntity> void insert(T obj) {
		super.getHibernateTemplate().save(obj);
		if (logger.isDebugEnabled()) {
			logger.debug("insert(" + obj.getClass().getName() + ")");
		}
	}

	@Override
	public <T extends IEntity> void update(T obj) {
		super.getHibernateTemplate().update(obj);
		if (logger.isDebugEnabled()) {
			logger.debug("update(" + obj.getClass().getName() + ")");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List query(String ql, Object params, boolean isNamedQuery,
			boolean isNativeQuery, int firstResult, int maxResults) {
		Query query = null;
		if (isNamedQuery) {
			throw new UnsupportedOperationException(
					"Name query not support in hiberate DAO");
		} else if (isNativeQuery) {
			query = super.getSession().createSQLQuery(ql);
		} else {
			query = super.getSession().createQuery(ql);
		}
		if (params != null) {
			int index = 0;
			if (params instanceof Object[]) {
				Object[] parameters = (Object[]) params;
				for (Object value : parameters) {
					query.setParameter(index, value);
					index++;
				}
			} else if (params instanceof List) {
				List<?> parameters = (List<?>) params;
				for (Object value : parameters) {
					query.setParameter(index, value);
					index++;
				}
			} else if (params instanceof Map) {
				Map<String, Object> parameters = (Map<String, Object>) params;
				Iterator<String> it = parameters.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object value = parameters.get(key);
					query.setParameter(key, value);
				}
			}
		}
		if (firstResult >= 0) {
			query.setFirstResult(firstResult);
		}
		if (maxResults >= 0) {
			query.setMaxResults(maxResults);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public long updateByQuery(String ql, Object params, boolean isNamedQuery,
			boolean isNativeQuery) {
		Query query = null;
		if (isNamedQuery) {
			throw new UnsupportedOperationException(
					"Name query not support in hiberate DAO");
		} else if (isNativeQuery) {
			query = super.getSession().createSQLQuery(ql);
		} else {
			query = super.getSession().createQuery(ql);
		}
		if (params != null) {
			int index = 0;
			if (params instanceof Object[]) {
				Object[] parameters = (Object[]) params;
				for (Object value : parameters) {
					query.setParameter(index, value);
					index++;
				}
			} else if (params instanceof List) {
				List<?> parameters = (List<?>) params;
				for (Object value : parameters) {
					query.setParameter(index, value);
					index++;
				}
			} else if (params instanceof Map) {
				Map<String, Object> parameters = (Map<String, Object>) params;
				Iterator<String> it = parameters.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					Object value = parameters.get(key);
					query.setParameter(key, value);
				}
			}
		}
		return query.executeUpdate();
	}
}
