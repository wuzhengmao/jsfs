package org.mingy.kernel.facade.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.mingy.kernel.bean.IEntity;
import org.mingy.kernel.bean.ILogicDeletable;
import org.mingy.kernel.bean.ITimeTrackable;
import org.mingy.kernel.facade.IEntityDaoFacade;
import org.mingy.kernel.orm.ICommonDao;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class EntityDaoFacadeImpl implements IEntityDaoFacade {

	private ICommonDao commonDao;

	/**
	 * 判断当前Bean对象中的Key是否为空，或者为空定义。<br>
	 * 后续程序可能通过判断此属性，来确定对数据进行何种操作。 空有以下几种定义：<br>
	 * 1. Key == null<br>
	 * 2. Key == -1l (Long)<br>
	 * 3. Key == -1 (Integer)<br>
	 * 4. Key == "" (String)
	 * 
	 * @param key
	 *            需要判断的关键字
	 * 
	 * @return 如果不属于上述定义，则返回false；否则返回true。
	 */
	private static boolean isNull(Serializable key) {
		if (key == null) {
			return true;
		} else if (key instanceof Long) {
			long id = ((Long) key).longValue();
			return id == -1l;
		} else if (key instanceof Integer) {
			int id = ((Integer) key).intValue();
			return id == -1;
		} else if (key instanceof String) {
			String id = (String) key;
			return id.length() <= 0;
		} else {
			return false;
		}
	}

	/**
	 * 初始化懒加载属性或集合。
	 * 
	 * @param bean
	 *            数据实体
	 * @param fieldName
	 *            懒加载属性，支持","分隔
	 */
	private void initializeField(Object bean, String fieldName) {
		if (bean != null) {
			Assert.hasText(fieldName, "field name required");
			String[] nestedFields = StringUtils.tokenizeToStringArray(
					fieldName, ".");
			Object value = bean;
			for (int i = 0; i < nestedFields.length; i++) {
				if (value == null)
					break;
				try {
					value = PropertyUtils.getProperty(value, nestedFields[i]);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			if (value instanceof HibernateProxy) {
				LazyInitializer initializer = ((HibernateProxy) value)
						.getHibernateLazyInitializer();
				initializer.initialize();
			} else if (value instanceof PersistentCollection) {
				PersistentCollection bag = (PersistentCollection) value;
				bag.forceInitialization();
			}
		}
	}

	@Override
	public <T extends IEntity> void deleteAll(Class<T> cls) {
		commonDao.deleteAll(cls);
	}

	@Override
	public <T extends ILogicDeletable> void logicDeleteAll(Class<T> cls) {
		String hql = "UPDATE " + cls.getName() + " t SET t.valid = ?";
		if (ITimeTrackable.class.isAssignableFrom(cls))
			hql += ", t.lastUpdateTime = ?";
		commonDao.updateByQuery(hql,
				ITimeTrackable.class.isAssignableFrom(cls) ? new Object[] {
						false, new Date() } : new Object[] { false }, false,
				false);
	}

	@Override
	public <T extends IEntity> void delete(Class<T> cls, Serializable key) {
		commonDao.delete(cls, key);
	}

	@Override
	public <T extends ILogicDeletable> void logicDelete(Class<T> cls,
			Serializable key) {
		T object = commonDao.findByKey(cls, key);
		if (object != null) {
			logicDelete(object);
		}
	}

	@Override
	public <T extends IEntity> void delete(T object) {
		commonDao.delete(object);
	}

	@Override
	public <T extends ILogicDeletable> void logicDelete(T object) {
		object.setValid(false);
		if (object instanceof ITimeTrackable) {
			((ITimeTrackable) object).setLastUpdateTime(new Date());
		}
		commonDao.update(object);
	}

	@Override
	public <T extends IEntity> void delete(Class<T> cls, Serializable[] keys) {
		for (Serializable key : keys) {
			commonDao.delete(cls, key);
		}
	}

	@Override
	public <T extends ILogicDeletable> void logicDelete(Class<T> cls,
			Serializable[] keys) {
		for (Serializable key : keys) {
			T object = commonDao.findByKey(cls, key);
			if (object != null) {
				logicDelete(object);
			}
		}
	}

	@Override
	public long deleteByQuery(String ql, List<Object> params,
			boolean isNamedQuery) {
		return commonDao.updateByQuery(ql, params, isNamedQuery, false);
	}

	@Override
	public long deleteByQuery(String ql, Map<String, Object> params,
			boolean isNamedQuery) {
		return commonDao.updateByQuery(ql, params, isNamedQuery, false);
	}

	@Override
	public long deleteByQuery(String ql, Object[] params, boolean isNamedQuery) {
		return commonDao.updateByQuery(ql, params, isNamedQuery, false);
	}

	@Override
	public <T extends IEntity> List<T> loadAll(Class<T> cls) {
		return commonDao.findAll(cls);
	}

	@Override
	public <T extends IEntity> List<T> loadAll(Class<T> cls, String[] lazyFields) {
		List<T> list = loadAll(cls);
		for (T entity : list) {
			for (String f : lazyFields) {
				initializeField(entity, f);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ILogicDeletable> List<T> loadAll(Class<T> cls,
			boolean valid) {
		String jpql = "FROM " + cls.getName() + " t WHERE t.valid = ?";
		return commonDao.query(jpql, new Object[] { valid }, false, false, -1,
				-1);
	}

	@Override
	public <T extends ILogicDeletable> List<T> loadAll(Class<T> cls,
			boolean valid, String[] lazyFields) {
		List<T> list = loadAll(cls, valid);
		for (T entity : list) {
			for (String f : lazyFields) {
				initializeField(entity, f);
			}
		}
		return list;
	}

	@Override
	public <T extends IEntity> List<T> loadAll(Class<T> cls, Object key,
			String field) {
		return loadAll(cls, new Object[] { key }, new String[] { field });
	}

	@Override
	public <T extends IEntity> List<T> loadAll(Class<T> cls, Object key,
			String field, String[] lazyFields) {
		return loadAll(cls, new Object[] { key }, new String[] { field },
				lazyFields);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IEntity> List<T> loadAll(Class<T> cls, Object[] keys,
			String[] fields) {
		if (keys == null || keys.length == 0)
			throw new IllegalArgumentException("keys can not be empty");
		if (fields == null || fields.length == 0)
			throw new IllegalArgumentException("fields can not be empty");
		StringBuilder sb = new StringBuilder("SELECT e FROM ").append(
				cls.getName()).append(" AS e WHERE");
		for (int i = 0; i < Math.min(keys.length, fields.length);) {
			if (i > 0)
				sb.append(" AND");
			sb.append(" e.").append(fields[i]).append(" = ?").append(++i);
		}
		return (List<T>) commonDao.query(sb.toString(), keys, false, false, -1,
				-1);
	}

	@Override
	public <T extends IEntity> List<T> loadAll(Class<T> cls, Object[] keys,
			String[] fields, String[] lazyFields) {
		List<T> list = loadAll(cls, keys, fields);
		for (T entity : list) {
			for (String f : lazyFields) {
				initializeField(entity, f);
			}
		}
		return list;
	}

	@Override
	public <T extends IEntity> T load(Class<T> cls, Serializable key) {
		return commonDao.findByKey(cls, key);
	}

	@Override
	public <T extends IEntity> T load(Class<T> cls, Serializable key,
			String[] lazyFields) {
		T entity = load(cls, key);
		for (String f : lazyFields) {
			initializeField(entity, f);
		}
		return entity;
	}

	@Override
	public <T extends IEntity> T load(Class<T> cls, Object key, String field) {
		return load(cls, new Object[] { key }, new String[] { field });
	}

	@Override
	public <T extends IEntity> T load(Class<T> cls, Object key, String field,
			String[] lazyFields) {
		return load(cls, new Object[] { key }, new String[] { field },
				lazyFields);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IEntity> T load(Class<T> cls, Object[] keys,
			String[] fields) {
		if (keys == null || keys.length == 0)
			throw new IllegalArgumentException("keys can not be empty");
		if (fields == null || fields.length == 0)
			throw new IllegalArgumentException("fields can not be empty");
		StringBuilder sb = new StringBuilder("SELECT e FROM ").append(
				cls.getName()).append(" AS e WHERE");
		for (int i = 0; i < Math.min(keys.length, fields.length);) {
			if (i > 0)
				sb.append(" AND");
			sb.append(" e.").append(fields[i]).append(" = ?").append(++i);
		}
		List<T> list = (List<T>) commonDao.query(sb.toString(), keys, false,
				false, 0, 1);
		return !list.isEmpty() ? list.get(0) : null;
	}

	@Override
	public <T extends IEntity> T load(Class<T> cls, Object[] keys,
			String[] fields, String[] lazyFields) {
		T entity = load(cls, keys, fields);
		for (String f : lazyFields) {
			initializeField(entity, f);
		}
		return entity;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String ql, List<Object> params, boolean isNamedQuery) {
		return commonDao.query(ql, params, isNamedQuery, false, -1, -1);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String ql, List<Object> params, boolean isNamedQuery,
			int firstResult, int maxResults) {
		return commonDao.query(ql, params, isNamedQuery, false, firstResult,
				maxResults);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String ql, Map<String, Object> params,
			boolean isNamedQuery) {
		return commonDao.query(ql, params, isNamedQuery, false, -1, -1);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String ql, Map<String, Object> params,
			boolean isNamedQuery, int firstResult, int maxResults) {
		return commonDao.query(ql, params, isNamedQuery, false, firstResult,
				maxResults);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String ql, Object[] params, boolean isNamedQuery) {
		return commonDao.query(ql, params, isNamedQuery, false, -1, -1);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String ql, Object[] params, boolean isNamedQuery,
			int firstResult, int maxResults) {
		return commonDao.query(ql, params, isNamedQuery, false, firstResult,
				maxResults);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String ql, Object params, boolean isNamedQuery,
			boolean isNativeQuery, int firstResult, int maxResults) {
		return commonDao.query(ql, params, isNamedQuery, isNativeQuery,
				firstResult, maxResults);
	}

	@Override
	public <T extends IEntity> void save(T object) {
		if (isNull(object.getId())) {
			if (object instanceof ITimeTrackable) {
				Date now = new Date();
				((ITimeTrackable) object).setCreateTime(now);
				((ITimeTrackable) object).setLastUpdateTime(now);
			}
			commonDao.insert(object);
		} else {
			if (object instanceof ITimeTrackable) {
				Date now = new Date();
				((ITimeTrackable) object).setLastUpdateTime(now);
			}
			commonDao.update(object);
		}
	}

	@Override
	public void updateByQuery(String ql, List<Object> params,
			boolean isNamedQuery) {
		commonDao.updateByQuery(ql, params, isNamedQuery, false);
	}

	@Override
	public void updateByQuery(String ql, Map<String, Object> params,
			boolean isNamedQuery) {
		commonDao.updateByQuery(ql, params, isNamedQuery, false);
	}

	@Override
	public void updateByQuery(String ql, Object[] params, boolean isNamedQuery) {
		commonDao.updateByQuery(ql, params, isNamedQuery, false);
	}

	@Override
	public long updateByQuery(String ql, Object params, boolean isNamedQuery,
			boolean isNativeQuery) {
		return commonDao.updateByQuery(ql, params, isNamedQuery, isNativeQuery);
	}

	public void setCommonDao(ICommonDao commonDao) {
		this.commonDao = commonDao;
	}
}
