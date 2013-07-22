package org.mingy.kernel.orm.impl.jpa;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mingy.kernel.bean.IEntity;
import org.mingy.kernel.orm.ICommonDao;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.util.Assert;

/**
 * 基于JPA的O-R映射的ICommonDao的接口实现
 * 
 * @author Mingy
 * 
 */
public class CommonDaoImpl implements ICommonDao {

	private static final Log logger = LogFactory.getLog(CommonDaoImpl.class);
	private static final int FIELD_BASE_INDEX = 1;
	private JpaTemplate template = null;

	@Override
	public <T extends IEntity> void delete(Class<T> clazz, Serializable key) {
		T attachObj = template.find(clazz, key);
		template.remove(attachObj);
		if (logger.isDebugEnabled()) {
			logger.debug("delete(" + clazz.getName() + ", " + key + ")");
		}
	}

	@Override
	public <T extends IEntity> void delete(T obj) {
		delete(obj.getClass(), obj.getId());
	}

	@Override
	public <T extends IEntity> void deleteAll(Class<T> cls) {
		String pql = "DELETE FROM " + cls.getName();
		// 执行批量删除操作
		long result = updateByQuery(pql, new Object[] {}, false, false);
		if (logger.isDebugEnabled()) {
			logger.debug("deleteAll(" + cls.getName() + "), total: " + result
					+ " rows.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IEntity> List<T> findAll(Class<T> cls) {
		String pql = "SELECT o FROM " + cls.getName() + " o";
		return query(pql, new Object[] {}, false, false, -1, -1);
	}

	@Override
	public <T extends IEntity> T findByKey(Class<T> cls, Serializable key) {
		return template.find(cls, key);
	}

	@Override
	public <T extends IEntity> void insert(T obj) {
		template.persist(obj);
		if (logger.isDebugEnabled()) {
			logger.debug("insert(" + obj.getClass().getName() + ")");
		}
	}

	@Override
	public <T extends IEntity> void update(T obj) {
		template.merge(obj);
		if (logger.isDebugEnabled()) {
			logger.debug("update(" + obj.getClass().getName() + ")");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List query(final String ql, final Object params,
			final boolean isNamedQuery, final boolean isNativeQuery,
			final int firstResult, final int maxResults) {
		return template.executeFind(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				Query query = null;
				if (isNamedQuery) {
					query = entityManager.createNamedQuery(ql);
				} else if (isNativeQuery) {
					query = entityManager.createNativeQuery(ql);
				} else {
					query = entityManager.createQuery(ql);
				}
				if (params != null) {
					int index = FIELD_BASE_INDEX;
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
						Map<String, ?> parameters = (Map<String, ?>) params;
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
				return query.getResultList();
			}
		});
	}

	@Override
	public long updateByQuery(final String ql, final Object params,
			final boolean isNamedQuery, final boolean isNativeQuery) {
		return (Integer) template.execute(new JpaCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				Query query = null;
				if (isNamedQuery) {
					query = entityManager.createNamedQuery(ql);
				} else if (isNativeQuery) {
					query = entityManager.createNativeQuery(ql);
				} else {
					query = entityManager.createQuery(ql);
				}
				if (params != null) {
					int index = FIELD_BASE_INDEX;
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
						Map<String, ?> parameters = (Map<String, ?>) params;
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
		});
	}

	/**
	 * 通过外部注入实体管理器工厂
	 * 
	 * @param factory
	 *            实体管理器工厂
	 */
	public void setEntityManagerFactory(EntityManagerFactory factory) {
		Assert.notNull(factory);
		template = new JpaTemplate(factory);
	}
}
